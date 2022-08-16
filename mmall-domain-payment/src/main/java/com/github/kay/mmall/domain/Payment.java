package com.github.kay.mmall.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.kay.mmall.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * 支付对象
 */
@Setter
@Getter
@Entity
public class Payment extends BaseEntity {

    @JsonIgnore
    public static final transient String PAY_ID_PREFIX = "pay:";

    private Date createTime;

    private Date updateTime;

    private String payId;

    private BigDecimal totalPrice;

    private Long expires; // ms

    private String paymentLink;

    private State payState;

    public Payment() {
    }

    public Payment(BigDecimal totalPrice, Long expires,@NotNull Integer accountId) {
        setTotalPrice(totalPrice);
        setExpires(expires);
        setCreateTime(new Date());
        setPayState(State.WAITING);

        // 下面这两个是随便写的，实际应该根据情况调用支付服务，返回待支付的ID
        setPayId(PAY_ID_PREFIX + UUID.randomUUID().toString());
        // 产生支付单的时候一定是有用户的
        setPaymentLink("/pay/modify/" + getPayId() + "?state=PAYED&accountId=" + accountId);
    }

    @PreUpdate
    @PrePersist
    public void updateTime(){
        this.updateTime = new Date();
    }

    public enum State {
        /**
         * 等待支付中
         */
        WAITING,
        /**
         * 已取消
         */
        CANCEL,
        /**
         * 已支付
         */
        PAYED,
        /**
         * 已超时回滚（未支付，并且商品已恢复）
         */
        TIMEOUT
    }
}
