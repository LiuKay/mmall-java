package com.kay.service;

import com.kay.common.ServerResponse;
import com.kay.vo.CartVo;

/**
 * Created by kay on 2018/3/23.
 */
public interface CartService {
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteByProductIds(Integer userid, String productIds);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer status);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}
