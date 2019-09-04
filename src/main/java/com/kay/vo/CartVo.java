package com.kay.vo;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * Created by kay on 2018/3/23.
 * 购物车返回对象
 */
@Data
public class CartVo {

    List<CartProductVo> cartProductVoList;

    private BigDecimal cartTotalPrice;

    private Boolean allChecked;  //全选状态

    private String imageHost;

}
