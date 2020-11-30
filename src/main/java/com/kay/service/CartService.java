package com.kay.service;

import com.kay.common.ChoiceEnum;
import com.kay.vo.CartVo;
import org.springframework.lang.NonNull;

/**
 * Created by kay on 2018/3/23.
 */
public interface CartService {
    CartVo add(Integer userId, @NonNull Integer productId, @NonNull Integer count);

    CartVo update(Integer userId, @NonNull Integer productId, @NonNull Integer count);

    CartVo deleteByProductIds(Integer userid, @NonNull String productIds);

    CartVo list(Integer userId);

    CartVo selectOrUnSelect(Integer userId, Integer productId, ChoiceEnum choice);

    Integer getCartProductCount(Integer userId);
}
