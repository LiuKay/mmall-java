package com.kay.vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * Created by kay on 2018/3/28.
 */
@Data
public class OrderItemVo {

    private Long orderNo;

    private Integer productId;

    private String productName;
    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private String createTime;
}
