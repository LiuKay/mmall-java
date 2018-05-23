package com.kay.task;

import com.kay.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by kay on 2018/5/23.
 * 定时关闭未支付订单
 */

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    @Scheduled(cron = "0 */1 * * * ? ")
    public void closeOrder() {
        log.info("定时任务启动");

        log.info("定时任务结束");
    }
}
