package com.github.kay.mmall.domain.product;


import com.github.kay.mmall.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class Advertisement extends BaseEntity {

    @NotEmpty(message = "广告图片不允许为空")
    private String image;

    @NotNull(message = "广告应当有关联的商品")
    @Column(name = "product_id")
    private Integer productId;

}
