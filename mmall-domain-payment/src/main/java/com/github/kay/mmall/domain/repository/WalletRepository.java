package com.github.kay.mmall.domain.repository;

import com.github.kay.mmall.domain.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Integer> {

    Optional<Wallet> findByAccountId(Integer accountId);

}
