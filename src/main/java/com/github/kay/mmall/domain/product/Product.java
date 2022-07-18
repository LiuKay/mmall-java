package com.github.kay.mmall.domain.product;

import com.github.kay.mmall.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Setter
@Getter
@Entity
public class Product extends BaseEntity {

    @NotEmpty(message = "商品名称不允许为空")
    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    @NotNull(message = "商品应当有明确的价格")
    @Min(value = 0,message = "price min is 0")
    private BigDecimal price;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Set<Specification> specifications;
}
