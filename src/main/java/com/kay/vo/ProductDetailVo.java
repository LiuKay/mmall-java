package com.kay.vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * Created by kay on 2018/3/20.
 * 商品详细VO
 */
@Data
public class ProductDetailVo {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private String subImages;
    private String detail;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String createTime;
    private String updateTime;


    private String imageHost;  //图片服务器地址
    private Integer parentCategoryId;
}
