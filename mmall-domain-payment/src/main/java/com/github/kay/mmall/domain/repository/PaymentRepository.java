package com.github.kay.mmall.domain.repository;

import com.github.kay.mmall.domain.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    Payment findByPayId(String payId);

}
