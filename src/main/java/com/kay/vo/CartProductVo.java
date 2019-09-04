package com.kay.vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * Created by kay on 2018/3/23.
 * 产品+购物车结合的Vo
 */
@Data
public class CartProductVo {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity; //数量
    private String productName;
    private String productSubtitle;
    private String productMainImage;
    private BigDecimal productPrice;
    private Integer productStatus;
    private BigDecimal productTotalPrice; //单价*数量
    private Integer productStock; //库存
    private Integer productChecked;//是否勾选

    private String limitQuantity; //数量限制的标志，是否大于库存
}
