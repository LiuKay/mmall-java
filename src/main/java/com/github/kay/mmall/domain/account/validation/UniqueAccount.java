package com.github.kay.mmall.domain.account.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER,ElementType.TYPE})
@Constraint(validatedBy = AccountValidator.UniqueAccountValidator.class)
public @interface UniqueAccount {
    String message() default "用户名、邮箱、手机号均不能与现有用户重复";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
