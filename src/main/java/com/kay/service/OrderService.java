package com.kay.service;

import com.github.pagehelper.PageInfo;
import com.kay.vo.OrderProductVo;
import com.kay.vo.OrderVo;
import java.util.Map;

/**
 * Created by kay on 2018/3/27.
 */
public interface OrderService {
    Map<String, String> pay(Integer userId, Long orderNo, String path);

    void alipayCallback(Map<String, String> params);

    boolean isOrderPaid(Integer userId, Long orderNo);

    OrderVo createOrder(Integer userId, Integer addressId);

    void cancelOrder(Integer userId, Long orderNo);

    OrderProductVo getOrderCartProduct(Integer userId);

    OrderVo getOrderDetail(Integer userId, Long orderNo);

    PageInfo<OrderVo> getOrderList(Integer userId, int pageNum, int pageSize);

    PageInfo getManageList(int pageNum, int pageSize);

    OrderVo getManageDetail(Long orderNo);

    PageInfo getManageSearch(Long orderNo, int pageNum, int pageSize);

    void getManageSendGoods(Long orderNo);

    //v2.0 增加定时关闭订单
    void closeOrder(int hour);
}
