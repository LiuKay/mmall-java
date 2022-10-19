package com.github.kay.mmall.payment.domain.validation;

import com.github.kay.mmall.dto.Settlement;
import com.github.kay.mmall.payment.domain.client.ProductServiceClient;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证库存是否足够
 */
public class SettlementValidator implements ConstraintValidator<ValidStock, Settlement> {

    @Autowired
    private ProductServiceClient stockpileService;

    @Override
    public boolean isValid(Settlement settlement, ConstraintValidatorContext constraintValidatorContext) {
        return settlement.getItems()
                         .stream()
                         .noneMatch(i -> stockpileService.queryStockpile(i.getProductId())
                                                         .getAmount() < i.getAmount());
    }
}
