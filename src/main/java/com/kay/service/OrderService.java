package com.kay.service;

import com.github.pagehelper.PageInfo;
import com.kay.common.ServerResponse;
import com.kay.vo.OrderVo;
import java.util.Map;

/**
 * Created by kay on 2018/3/27.
 */
public interface OrderService {
    ServerResponse pay(Integer userId, Long orderNo, String path);

    ServerResponse alipayCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    ServerResponse createOrder(Integer userId, Integer shippingId);

    ServerResponse cancleOrder(Integer userId, Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    ServerResponse<PageInfo> getManageList(int pageNum, int pageSize);

    ServerResponse<OrderVo> getManageDetail(Long orderNo);

    ServerResponse<PageInfo> getManageSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse<OrderVo> getManageSendGoods(Long orderNo);

    //v2.0 增加定时关闭订单
    void closeOrder(int hour);
}
