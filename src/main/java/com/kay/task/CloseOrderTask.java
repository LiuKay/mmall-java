package com.kay.task;

import com.kay.common.Const;
import com.kay.common.RedisShardedPool;
import com.kay.service.IOrderService;
import com.kay.util.PropertiesUtil;
import com.kay.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by kay on 2018/5/23.
 * 定时关闭未支付订单
 *
 * 注意分布式环境下，如何防止死锁问题
 */

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    //@Scheduled(cron = "0 */1 * * * ? ")
    public void closeOrderTaskV1() {
        log.info("定时任务启动");

        log.info("定时任务结束");
    }


    @Scheduled(cron = "0/10 * * * * ? ")
    public void closeOrderTaskV2() {
        log.info("定时任务启动");
        long lockTime = Long.valueOf(PropertiesUtil.getProperty("redis.lock.time", "50000"));
        Long lockKeyResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTime));

        //如果获得了分布式锁，执行关单业务
        if (lockKeyResult != null && lockKeyResult == 1) {
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            log.info("没有获得分布式锁");
        }
        log.info("定时任务结束================================");
    }

    private void closeOrder(String lockName) {
        RedisShardedPoolUtil.expire(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,50); //锁住50秒
        log.info("线程{} 获取锁 {}",Thread.currentThread().getName(),Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);

        //执行关单操作
        //主动关闭锁
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("线程{} 释放锁 {}",Thread.currentThread().getName(),Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

}
