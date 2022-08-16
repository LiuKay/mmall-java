package com.github.kay.mmall.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kay.mmall.domain.product.Product;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Map;

//支付订单     TODO: 支付订单放在了缓存中，没有做持久化
@Data
public class Settlement {

    @Size(min = 1, message = "结算单中缺少商品清单")
    private Collection<Item> items;

    @NotNull(message = "结算单中缺少配送信息")
    private Purchase purchase;

    /**
     * 购物清单中的商品信息
     * 基于安全原因（避免篡改价格），改信息不会取客户端的，需在服务端根据商品ID再查询出来
     */
    public transient Map<Integer, Product> productMap;

    /**
     * 购买的商品
     */
    @Data
    public static class Item {
        @NotNull(message = "结算单中必须有明确的商品数量")
        @Min(value = 1, message = "结算单中商品数量至少为一件")
        private Integer amount;

        @JsonProperty("id")
        @NotNull(message = "结算单中必须有明确的商品信息")
        private Integer productId;
    }

    @Data
    public static class Purchase{

        private Boolean delivery = true;

        @NotEmpty(message = "配送信息中缺少支付方式")
        private String pay;

        @NotEmpty(message = "配送信息中缺少收件人姓名")
        private String name;

        @NotEmpty(message = "配送信息中缺少收件人电话")
        private String telephone;

        @NotEmpty(message = "配送信息中缺少收件地址")
        private String location;
    }
}
