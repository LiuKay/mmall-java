package com.github.kay.mmall.payment.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Constraint(validatedBy = SettlementValidator.class)
public @interface ValidStock {

    String message() default "商品库存不足";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
