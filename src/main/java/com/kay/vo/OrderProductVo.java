package com.kay.vo;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * Created by kay on 2018/3/28.
 */
@Data
public class OrderProductVo {

    private List<OrderItemVo> orderItemVoList;

    private BigDecimal productTotalPrice;

    private String imageHost;

}
