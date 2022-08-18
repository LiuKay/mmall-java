package com.github.kay.mmall.payment.controller;

import com.github.kay.mmall.payment.domain.application.PaymentApplicationService;
import com.github.kay.mmall.domain.account.Account;
import com.github.kay.mmall.domain.security.Role;
import com.github.kay.mmall.payment.domain.Payment;
import com.github.kay.mmall.infrasucture.common.CodedMessage;
import com.github.kay.mmall.infrasucture.common.CommonResponse;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restful/pay")
public class PaymentController {

    private final PaymentApplicationService service;

    public PaymentController(PaymentApplicationService service) {
        this.service = service;
    }

    /**
     * 修改支付单据的状态
     */
    @PatchMapping("/{payId}")
    @Secured(Role.USER)
    public ResponseEntity<CodedMessage> updatePaymentState(@PathVariable("payId") String payId, @RequestParam("state") Payment.State state) {
        Account account = (Account) SecurityContextHolder.getContext()
                                                         .getAuthentication()
                                                         .getPrincipal();
        return updatePaymentStateAlias(payId, account.getId(), state);
    }

    /**
     * 修改支付单状态的GET方法别名
     * 考虑到该动作要由二维码扫描来触发，只能进行GET请求，所以增加一个别名以便通过二维码调用
     * 这个方法原本应该作为银行支付接口的回调，不控制调用权限（谁付款都行），但都认为是购买用户付的款
     */
    @GetMapping("/modify/{payId}")
    public ResponseEntity<CodedMessage> updatePaymentStateAlias(@PathVariable("payId") String payId,
                                                                @RequestParam("accountId") Integer accountId,
                                                                @RequestParam("state") Payment.State state) {
        if (state == Payment.State.PAYED) {
            return CommonResponse.op(() -> service.accomplishPayment(accountId, payId));
        } else {
            return CommonResponse.op(() -> service.cancelPayment(payId));
        }
    }

}
