package com.github.kay.mmall.domain.payment;

import com.github.kay.mmall.application.payment.dto.Settlement;
import com.github.kay.mmall.domain.account.Account;
import com.github.kay.mmall.infrasucture.CacheConfiguration;
import com.github.kay.mmall.infrasucture.redis.RedisKeyExpirationHandler;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PaymentService {

    private static final long DEFAULT_PRODUCT_FROZEN_EXPIRES = CacheConfiguration.SYSTEM_DEFAULT_EXPIRES / 2;
    private static final String PAYMENT_TEMP_KEY_PREFIX = "TEMP:";
    private static final String LOCK_PREFIX = "LOCK:";

    private final PaymentRepository paymentRepository;
    private final StockpileService stockpileService;
    private final Cache settlementCache;


    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public PaymentService(PaymentRepository paymentRepository,
                          StockpileService stockpileService,
                          @Qualifier("settlement") Cache settlementCache) {
        this.paymentRepository = paymentRepository;
        this.stockpileService = stockpileService;
        this.settlementCache = settlementCache;
    }

    public Payment producePayment(Settlement settlement) {
        final BigDecimal totalPrice = settlement.getItems()
                                                .stream()
                                                .map(i -> {
                                                    stockpileService.frozen(i.getProductId(),
                                                                            i.getAmount());
                                                    return settlement.productMap.get(i.getProductId())
                                                                                .getPrice()
                                                                                .multiply(new BigDecimal(
                                                                                        i.getAmount()));
                                                })
                                                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //TODO:忽略运费

        Account account= (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Payment payment = new Payment(totalPrice, DEFAULT_PRODUCT_FROZEN_EXPIRES, account.getId());
        paymentRepository.save(payment);

        settlementCache.put(payment.getPayId(), settlement);

        log.info("创建支付订单:{}，总额：{}", payment.getPayId(), payment.getTotalPrice().toString());
        return payment;
    }

    public BigDecimal finish(String payId) {
        return executeWithLock(redissonClient.getLock(LOCK_PREFIX + payId), () -> {
            final Payment payment = paymentRepository.findByPayId(payId);
            if (Payment.State.WAITING == payment.getPayState()) {
                payment.setPayState(Payment.State.PAYED);
                paymentRepository.save(payment);

                clearPaymentCache(payment);

                log.info("订单[{}]已处理完成，等待支付", payId);
                return payment.getTotalPrice();
            } else {
                throw new UnsupportedOperationException(
                        String.format("Not allowed pay current order: payId:%s, state:%s", payId,
                                      payment.getPayState()));
            }
        });
    }

    public void accomplishSettlement(Payment.State state, String payId) {
        final Settlement settlement = (Settlement) Objects.requireNonNull(Objects.requireNonNull(settlementCache.get(payId))
                                                                                 .get());
        settlement.getItems().forEach(i->{
            if (state == Payment.State.PAYED) {
                //减库存
                stockpileService.decrease(i.getProductId(), i.getAmount());
            }else {
                //释放库存
                stockpileService.thawed(i.getProductId(), i.getAmount());
            }
        });
    }

    public void cancel(String payId) {
        executeWithLock(redissonClient.getLock(LOCK_PREFIX + payId), () -> {
            Payment payment = paymentRepository.findByPayId(payId);
            if (payment.getPayState() == Payment.State.WAITING) {
                payment.setPayState(Payment.State.CANCEL);
                paymentRepository.save(payment);

                clearPaymentCache(payment);

                accomplishSettlement(Payment.State.CANCEL, payment.getPayId());
                log.info("编号为[{}]的支付单已被取消", payId);
            } else {
                throw new UnsupportedOperationException("当前订单不允许取消，当前状态为：" + payment.getPayState());
            }
            return null;
        });
    }

    private <T> T executeWithLock(RLock lock, Callable<T> callable) {
        try {
            if (!lock.tryLock(0, 10, TimeUnit.SECONDS)) {
                log.info("Lock is held by others.");
                return null;
            }

            return callable.call();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception exception) {
            throw new CompletionException(exception);
        } finally {
            lock.unlock();
        }

        return null;
    }

    /**
     * 在 Redis 中设置好过期时间等待过期事件触发，自动冲消未支持订单
     * 设置支付单自动冲销解冻的触发器
     * <p>
     * 如果在触发器超时之后，如果支付单未仍未被支付（状态是WAITING）
     * 则自动执行冲销，将冻结的库存商品解冻，以便其他人可以购买，并将Payment的状态修改为ROLLBACK。
     * <p>
     * 注意：
     * 使用TimerTask意味着节点带有状态，这在分布式应用中是必须明确【反对】的，如以下缺陷：
     * 1. 如果要考虑支付订单的取消场景，无论支付状态如何，这个TimerTask到时间之后都应当被执行。不应尝试使用TimerTask::cancel来取消任务。
     * 因为只有带有上下文状态的节点才能完成取消操作，如果要在集群中这样做，就必须使用支持集群的定时任务（如Quartz）以保证多节点下能够正常取消任务。
     * 2. 如果节点被重启、同样会面临到状态的丢失，导致一部分处于冻结的触发器永远无法被执行，所以需要系统启动时根据数据库状态有一个恢复TimeTask的的操作
     * 3. 即时只考虑正常支付的情况，真正生产环境中这种代码需要一个支持集群的同步锁（如用Redis实现互斥量），避免解冻支付和该支付单被完成两个事件同时在不同的节点中发生
     */
    public void setupAutoThawedTrigger(Payment payment) {
        storePaymentToCache(payment);
    }

    private void storePaymentToCache(Payment payment) {
        //create expiration key
        redisTemplate.opsForValue()
                     .set(payment.getPayId(), payment.getId(), payment.getExpires(),
                          TimeUnit.SECONDS);

        //create payment key, the key is used to close order
        final String tempPaymentKey = PAYMENT_TEMP_KEY_PREFIX + payment.getPayId();
        final RBucket<Integer> bucket = redissonClient.getBucket(tempPaymentKey);
        bucket.set(payment.getId());
    }

    private void clearPaymentCache(Payment payment) {
        redisTemplate.delete(payment.getPayId());

        final String tempPaymentKey = PAYMENT_TEMP_KEY_PREFIX + payment.getPayId();
        final RBucket<Integer> bucket = redissonClient.getBucket(tempPaymentKey);
        bucket.delete();
    }

    @Component
    public class AutoCloseOrderService implements RedisKeyExpirationHandler{

        @Override
        public boolean match(String key) {
            return key.startsWith(Payment.PAY_ID_PREFIX);
        }

        @Override
        public void handle(String key) {
            executeWithLock(redissonClient.getLock(LOCK_PREFIX + key), () -> {
                final String tempPaymentKey = PAYMENT_TEMP_KEY_PREFIX + key;
                final RBucket<Integer> bucket = redissonClient.getBucket(tempPaymentKey);
                final Integer paymentId = bucket.get();
                if (paymentId != null) {
                    Payment currentPayment = paymentRepository.findById(paymentId).orElseThrow(() -> new EntityNotFoundException(key));
                    if (currentPayment.getPayState() == Payment.State.WAITING) {
                        log.info("支付单{}当前状态为：WAITING，转变为：TIMEOUT", paymentId);
                        accomplishSettlement(Payment.State.TIMEOUT, key);
                    }
                }
                return null;
            });
        }
    }

}
