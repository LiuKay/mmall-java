package com.github.kay.mmall.payment.domain.repo;

import com.github.kay.mmall.payment.domain.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Integer> {

    Optional<Wallet> findByAccountId(Integer accountId);

}
