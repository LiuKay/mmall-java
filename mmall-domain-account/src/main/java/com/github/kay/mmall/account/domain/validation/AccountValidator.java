package com.github.kay.mmall.account.domain.validation;

import com.github.kay.mmall.account.domain.AccountRepository;
import com.github.kay.mmall.domain.account.Account;
import com.github.kay.mmall.domain.auth.AuthenticAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Predicate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountValidator<T extends Annotation> implements ConstraintValidator<T, Account> {

    @Autowired
    protected AccountRepository repository;

    protected Predicate<Account> predicate = c -> true;

    @Override
    public boolean isValid(Account account, ConstraintValidatorContext constraintValidatorContext) {
        // 在JPA持久化时，默认采用Hibernate实现，插入、更新时都会调用BeanValidationEventListener进行验证
        // 而验证行为应该尽可能在外层进行，Resource中已经通过@Vaild注解触发过一次验证，这里会导致重复执行
        // 正常途径是使用分组验证避免，但@Vaild不支持分组，@Validated支持，却又是Spring的私有标签
        // 另一个途径是设置Hibernate配置文件中的javax.persistence.validation.mode参数为“none”，这个参数在Spring的yml中未提供桥接
        // 为了避免涉及到数据库操作的验证重复进行，在这里做增加此空值判断，利用Hibernate验证时验证器不是被Spring创建的特点绕开


        log.debug("AccountValidator:{}", this.getClass()
                                             .getSimpleName());

        return repository == null || predicate.test(account);
    }


    public static class UniqueAccountValidator extends AccountValidator<UniqueAccount>{
        @Override
        public void initialize(UniqueAccount constraintAnnotation) {
            predicate = c -> !repository.existsByUsernameOrEmailOrTelephone(c.getUsername(), c.getEmail(),
                                                                            c.getTelephone());
        }
    }

    public static class NotConflictAccountValidator extends AccountValidator<NotConflictAccount>{
        @Override
        public void initialize(NotConflictAccount constraintAnnotation) {
            predicate = c -> {
                final Collection<Account> accounts = repository.findByUsernameOrEmailOrTelephone(
                        c.getUsername(), c.getEmail(), c.getTelephone());

                return accounts.isEmpty() || (accounts.size() == 1 && accounts.iterator()
                                                                              .next()
                                                                              .getId()
                                                                              .equals(c.getId()));
            };
        }
    }

    public static class AuthenticatedAccountValidator extends AccountValidator<AuthenticatedAccount>{
        @Override
        public void initialize(AuthenticatedAccount constraintAnnotation) {
            predicate = c -> {
                final Object principal = SecurityContextHolder.getContext()
                                                              .getAuthentication()
                                                              .getPrincipal();

                if ("anonymousUser".equals(principal)) {
                    return false;
                }else {
                    AuthenticAccount account = (AuthenticAccount) principal;
                    return c.getId()
                            .equals(account.getId());
                }
            };
        }
    }


}
