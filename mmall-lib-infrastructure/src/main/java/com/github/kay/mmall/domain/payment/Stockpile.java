package com.github.kay.mmall.domain.payment;

import com.github.kay.mmall.domain.BaseEntity;
import com.github.kay.mmall.domain.product.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * 库存
 */
@Setter
@Getter
@Entity
public class Stockpile extends BaseEntity {

    private Integer amount;

    private Integer frozen;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private transient Product product;

    public void frozen(Integer number) {
        this.amount -= number;
        this.frozen += number;
    }

    public void thawed(Integer number) {
        frozen(-1 * number);
    }

    public void decrease(Integer number) {
        this.frozen -= number;
    }

    public void increase(Integer number) {
        this.amount += number;
    }
}
