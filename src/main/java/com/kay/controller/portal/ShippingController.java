package com.kay.controller.portal;

import com.kay.common.ServerResponse;
import com.kay.domain.Shipping;
import com.kay.service.AuthService;
import com.kay.service.ShippingService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kay on 2018/3/26.
 */
@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private ShippingService shippingService;
    private final AuthService authService;

    @Autowired
    public ShippingController(ShippingService shippingService, AuthService authService) {
        this.shippingService = shippingService;
        this.authService = authService;
    }

    private Integer getUserId(HttpServletRequest request) {
        return authService.getUserId(request);
    }

    @GetMapping("/add")
    public ServerResponse add(Shipping shipping, HttpServletRequest request) {
        return shippingService.add(getUserId(request), shipping);
    }

    @GetMapping("/del")
    public ServerResponse delete(Integer shippingId, HttpServletRequest request) {
        return shippingService.delete(getUserId(request), shippingId);
    }

    @GetMapping("/select")
    public ServerResponse select(Integer shippingId, HttpServletRequest request){
        return shippingService.select(getUserId(request), shippingId);
    }

    @GetMapping("/update")
    public ServerResponse update(Shipping shipping, HttpServletRequest request){
        return shippingService.update(getUserId(request), shipping);
    }

    @GetMapping("/list")
    public ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                               HttpServletRequest request){
        return shippingService.list(getUserId(request), pageNum, pageSize);
    }
}
