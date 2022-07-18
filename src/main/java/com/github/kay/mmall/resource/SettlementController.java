package com.github.kay.mmall.resource;

import com.github.kay.mmall.application.payment.PaymentApplicationService;
import com.github.kay.mmall.application.payment.dto.Settlement;
import com.github.kay.mmall.domain.auth.Role;
import com.github.kay.mmall.domain.payment.Payment;
import com.github.kay.mmall.domain.payment.validation.ValidStock;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
    public Payment createPayment(@ValidStock Settlement settlement) {
        return service.executeBySettlement(settlement);
    }
}
