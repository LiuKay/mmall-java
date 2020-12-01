package com.kay.common;

/**
 * Created by kay on 2018/3/19. TODO: need to replace by Enum
 */
@Deprecated
public class Const {

    public interface AlipayCallback {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public interface REDIS_LOCK {
        String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK";
    }

}
