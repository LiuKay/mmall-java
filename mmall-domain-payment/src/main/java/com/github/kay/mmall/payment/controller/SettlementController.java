package com.github.kay.mmall.payment.controller;

import com.github.kay.mmall.payment.domain.application.PaymentApplicationService;
import com.github.kay.mmall.domain.security.Role;
import com.github.kay.mmall.payment.domain.Payment;
import com.github.kay.mmall.payment.domain.validation.ValidStock;
import com.github.kay.mmall.dto.Settlement;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/restful/settlements")
public class SettlementController {

    private final PaymentApplicationService service;

    public SettlementController(PaymentApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Secured(value = Role.USER)
    public Payment createPayment(@RequestBody @ValidStock Settlement settlement) {
        return service.executeBySettlement(settlement);
    }
}
