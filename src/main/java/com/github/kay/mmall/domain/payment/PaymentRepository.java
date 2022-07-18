package com.github.kay.mmall.domain.payment;

import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    Payment findByPayId(String payId);

}
