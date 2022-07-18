package com.github.kay.mmall.domain.payment.validation;

import com.github.kay.mmall.application.payment.dto.Settlement;
import com.github.kay.mmall.domain.payment.StockpileService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证库存是否足够
 */
public class SettlementValidator implements ConstraintValidator<ValidStock, Settlement> {

    @Autowired
    private StockpileService stockpileService;

    @Override
    public boolean isValid(Settlement settlement, ConstraintValidatorContext constraintValidatorContext) {
        return settlement.getItems()
                         .stream()
                         .noneMatch(i -> stockpileService.getByProductId(i.getProductId())
                                                         .getAmount() < i.getAmount());
    }
}
