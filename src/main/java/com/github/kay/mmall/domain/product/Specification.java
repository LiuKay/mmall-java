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
public class Specification extends BaseEntity {

    @NotEmpty(message = "商品规格名称不允许为空")
    private String title;

    @NotEmpty(message = "商品规格内容不允许为空")
    private String value;

    @NotNull(message = "商品规格必须归属于指定商品")
    @Column(name = "product_id")
    private Integer productId;
}
