package com.kay.service;

import com.github.pagehelper.PageInfo;
import com.kay.common.ServerResponse;
import com.kay.pojo.Shipping;

/**
 * Created by kay on 2018/3/26.
 */
public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse delete(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize);

}
