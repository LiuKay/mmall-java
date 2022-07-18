package com.github.kay.mmall.domain.payment;

import com.github.kay.mmall.domain.BaseEntity;
import com.github.kay.mmall.domain.account.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Wallet extends BaseEntity {

    private BigDecimal money;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
