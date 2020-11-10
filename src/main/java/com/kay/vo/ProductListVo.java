package com.kay.vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * Created by kay on 2018/3/21.
 * 商品列表VO
 */
@Data
public class ProductListVo {
    private Integer id;
    private Integer categoryId;

    private String name;
    private String subtitle;
    private String mainImage;
    private BigDecimal price;

    private Integer status;

    private String imageHost;
}
