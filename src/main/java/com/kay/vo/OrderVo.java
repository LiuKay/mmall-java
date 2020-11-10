package com.kay.vo;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * Created by kay on 2018/3/28.
 */
@Data
public class OrderVo {

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private String paymentTypeDesc;
    private Integer postage;

    private Integer status;

    private String statusDesc;

    private String paymentTime;

    private String sendTime;

    private String endTime;

    private String closeTime;

    private String createTime;

    //订单的明细
    private List<OrderItemVo> orderItemVoList;

    private String imageHost;
    private Integer shippingId;
    private String receiverName;

    //收货地址详情对象
    private ShippingVo shippingVo;

}
