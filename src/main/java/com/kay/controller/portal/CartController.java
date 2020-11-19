package com.kay.controller.portal;

import com.kay.common.Const;
import com.kay.common.ServerResponse;
import com.kay.service.AuthService;
import com.kay.service.CartService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kay on 2018/3/23.
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    private final AuthService authService;

    @Autowired
    public CartController(CartService cartService, AuthService authService) {
        this.cartService = cartService;
        this.authService = authService;
    }

    /**
     * 添加产品
     * @param productId
     * @param count
     * @param request
     * @return
     */
    @GetMapping("/add")
    public ServerResponse add(Integer productId, Integer count, HttpServletRequest request) {
        return cartService.add(getUserId(request), productId, count);
    }

    /**
     * 更新数量
     * @param productId
     * @param count
     * @param request
     * @return
     */
    @GetMapping("/update")
    public ServerResponse update(Integer productId,Integer count,HttpServletRequest request){
        return cartService.update(getUserId(request), productId, count);
    }

    /**
     * 移除产品
     * @param productIds
     * @param request
     * @return
     */
    @GetMapping("/delete_product")
    public ServerResponse delete(String productIds,HttpServletRequest request){
        return cartService.deleteByProductIds(getUserId(request), productIds);
    }

    @GetMapping("/list")
    public ServerResponse list(HttpServletRequest request) {
        return cartService.list(getUserId(request));
    }

    @GetMapping("/select")
    public ServerResponse select(HttpServletRequest request,Integer productId) {
        return cartService.selectOrUnSelect(getUserId(request), productId, Const.Cart.CHECKED);
    }

    @GetMapping("/un_select")
    public ServerResponse unSelect(HttpServletRequest request,Integer productId) {
        return cartService.selectOrUnSelect(getUserId(request), productId, Const.Cart.UN_CHECKED);
    }

    @GetMapping("/select_all")
    public ServerResponse selectAll(HttpServletRequest request) {
        return cartService.selectOrUnSelect(getUserId(request), null, Const.Cart.CHECKED);
    }

    @GetMapping("/un_select_all")
    public ServerResponse unSelectAll(HttpServletRequest request) {
        return cartService.selectOrUnSelect(getUserId(request), null, Const.Cart.UN_CHECKED);
    }

    @GetMapping("/get_cart_product_count")
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request) {
        return cartService.getCartProductCount(getUserId(request));
    }

    private Integer getUserId(HttpServletRequest request) {
        return authService.getUserId(request);
    }

}
