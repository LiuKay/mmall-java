package com.github.kay.mmall.payment.domain.application;

import com.github.kay.mmall.payment.domain.Payment;
import com.github.kay.mmall.payment.domain.client.ProductServiceClient;
import com.github.kay.mmall.payment.domain.service.PaymentService;
import com.github.kay.mmall.payment.domain.service.WalletService;

import com.github.kay.mmall.dto.Settlement;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class PaymentApplicationService {

    private final PaymentService paymentService;
    private final ProductServiceClient productService;
    private final WalletService walletService;
    private final RedisTemplate<String, Object> redisTemplate;

    public PaymentApplicationService(PaymentService paymentService,
                                     ProductServiceClient productService,
                                     WalletService walletService,
                                     RedisTemplate<String, Object> redisTemplate) {
        this.paymentService = paymentService;
        this.productService = productService;
        this.walletService = walletService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据结算清单的内容执行，生成对应的支付单
     */
    public Payment executeBySettlement(Settlement bill) {
        // 从服务中获取商品的价格，计算要支付的总价（安全原因，这个不能由客户端传上来）
        productService.replenishProductInformation(bill);
        // 冻结部分库存（保证有货提供）,生成付款单
        Payment payment = paymentService.producePayment(bill);
        // 设立解冻定时器（超时未支付则释放冻结的库存和资金）
        paymentService.setupAutoThawedTrigger(payment);
        return payment;
    }

    /**
     * 完成支付
     * 立即取消解冻定时器，执行扣减库存和资金
     */
    public void accomplishPayment(Integer accountId, String payId) {
        // 订单从冻结状态变为派送状态，扣减库存
        BigDecimal price = paymentService.finish(payId);
        // 扣减货款
        walletService.decrease(accountId, price);
        // 支付成功的清除缓存
        redisTemplate.delete(payId);

    }


    /**
     * 取消支付
     * 立即触发解冻定时器，释放库存和资金
     */
    public void cancelPayment(String payId) {
        // 释放冻结的库存
        paymentService.cancel(payId);
        // 支付成功的清除缓存
        redisTemplate.delete(payId);
    }
}
