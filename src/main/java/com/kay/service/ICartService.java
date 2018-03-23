package com.kay.service;

import com.kay.common.ServerResponse;
import com.kay.vo.CartVo;

/**
 * Created by kay on 2018/3/23.
 */
public interface ICartService {
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
}
