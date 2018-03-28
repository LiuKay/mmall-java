package com.kay.service;

import com.kay.common.ServerResponse;

import java.util.Map;

/**
 * Created by kay on 2018/3/27.
 */
public interface IOrderService {
    ServerResponse pay(Integer userId, Long orderNo, String path);

    ServerResponse alipayCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
}
