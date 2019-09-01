package com.kay.task;

import com.kay.common.Const;
import com.kay.common.RedissonManager;
import com.kay.service.IOrderService;
import com.kay.util.PropertiesUtil;
import com.kay.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * Created by kay on 2018/5/23.
 * 定时关闭未支付订单
 *
 * 注意分布式环境下，如何防止死锁问题
 */

//@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedissonManager redissonManager;

    //@Scheduled(cron = "0 */1 * * * ? ")
    public void closeOrderTaskV1() {
        log.info("定时任务启动");

        log.info("定时任务结束");
    }

//    @PreDestroy
    public void delCloseLock(){
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("Tomcat shut down 释放锁 {}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

   // @Scheduled(cron = "0/10 * * * * ? ")
    public void closeOrderTaskV2() {
        log.info("定时任务启动");
        long lockTime = Long.valueOf(PropertiesUtil.getProperty("redis.lock.time", "5000"));
        Long lockKeyResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTime));

        //如果获得了分布式锁，执行关单业务
        if (lockKeyResult != null && lockKeyResult.intValue() == 1) {
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            log.info("没有获得分布式锁");
        }
        log.info("定时任务结束================================");
    }

   // @Scheduled(cron = "0/10 * * * * ? ")
    public void closeOrderTaskV3() {
        log.info("定时任务启动");
        long lockTime = Long.valueOf(PropertiesUtil.getProperty("redis.lock.time", "5000"));
        Long lockKeyResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTime));

        //如果获得了分布式锁，执行关单业务
        if (lockKeyResult != null && lockKeyResult.intValue() == 1) {
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            String lockValue1 = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            //查到锁的值并与当前时间比较检查其是否已经超时，若超时则可以重新获取锁
            if (lockValue1 != null && System.currentTimeMillis() > Long.valueOf(lockValue1)) {

                //通过用当前时间戳getset操作会给对应的key设置新的值并返回旧值，这是一个原子操作
                //redis返回nil,则说明该值已经无效
                String lockValue2 = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTime));

                if (lockValue2 == null || StringUtils.equals(lockValue1, lockValue2)) {
                    //获取锁成功
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                } else {
                    log.info("没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }

            log.info("没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("定时任务结束================================");
    }


//    @Scheduled(cron = "0/10 * * * * ? ")
    public void closeOrderTaskV4() {
        log.info("定时任务启动");
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        try {
            //todo 若任务执行时间过短，则有可能在等锁的过程中2个服务任务都会获取到锁，这与实际需要的功能不一致，故需要将waitTime设置为0
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.redis.lock.time","2"));
                iOrderService.closeOrder(hour);
            } else {
                log.info("Redisson分布式锁没有获取到锁:{},ThreadName :{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
           log.error("Redisson 获取分布式锁异常",e);
        }finally {
            if (!getLock) {
                return;
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁:{},ThreadName :{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        }
    }

    private void closeOrder(String lockName) {
        RedisShardedPoolUtil.expire(lockName,50); //锁住50秒
        log.info("线程{} 获取锁 {}",Thread.currentThread().getName(),Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);

        //执行关单操作
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.redis.lock.time","2"));
        iOrderService.closeOrder(hour);

        //主动关闭锁
        RedisShardedPoolUtil.del(lockName);
        log.info("线程{} 释放锁 {}",Thread.currentThread().getName(),Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

}
