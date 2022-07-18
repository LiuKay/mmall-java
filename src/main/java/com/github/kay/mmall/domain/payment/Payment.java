package com.github.kay.mmall.domain.payment;

import com.github.kay.mmall.domain.BaseEntity;
import com.github.kay.mmall.domain.account.Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Entity;
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

    private Date createTime;

    private String payId;

    private BigDecimal totalPrice;

    private Long expires;

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
        setPayId(UUID.randomUUID().toString());
        // 产生支付单的时候一定是有用户的
        setPaymentLink("/pay/modify/" + getPayId() + "?state=PAYED&accountId=" + accountId);
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
