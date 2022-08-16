package com.github.kay.mmall.payment.domain.repo;

import com.github.kay.mmall.payment.domain.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    Payment findByPayId(String payId);

}
