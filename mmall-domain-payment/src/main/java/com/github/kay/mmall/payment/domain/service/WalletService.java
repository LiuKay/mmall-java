package com.github.kay.mmall.payment.domain.service;

import com.github.kay.mmall.domain.account.Account;
import com.github.kay.mmall.payment.domain.Wallet;
import com.github.kay.mmall.payment.domain.repo.WalletRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WalletService {

    private final WalletRepository repository;

    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    public void decrease(Integer accountId, BigDecimal amount) {
        final Wallet wallet = repository.findByAccountId(accountId)
                                        .orElseGet(() -> {
                                            Wallet w = new Wallet();
                                            Account account = new Account();
                                            account.setId(accountId);
                                            w.setAccount(account);
                                            w.setMoney(BigDecimal.ZERO);
                                            repository.save(w);
                                            return w;
                                        });

        if (wallet.getMoney().compareTo(amount) > 0) {
            wallet.setMoney(wallet.getMoney().subtract(amount));
            repository.save(wallet);
            log.info("支付成功。用户余额：{}，本次消费：{}", wallet.getMoney(), amount);
        }else {
            throw new IllegalStateException("用户余额不足以支付，请先充值");
        }
    }

    /**
     * 账户资金增加（演示程序，没有做充值入口，实际这个方法无用）
     */
    public void increase(Integer accountId, Double amount) {
        //todo
    }

    // 以下两个方法是为TCC事务准备的，在单体架构中不需要实现

    /**
     * 账户资金冻结
     * 从正常资金中移动指定数量至冻结状态
     */
    public void frozen(Integer accountId, Double amount) {
        //todo
    }

    /**
     * 账户资金解冻
     * 从冻结资金中移动指定数量至正常状态
     */
    public void thawed(Integer accountId, Double amount) {
        //todo
    }

}

