package com.kay.controller.portal;

import com.kay.common.ChoiceEnum;
import com.kay.service.AuthService;
import com.kay.service.CartService;
import com.kay.vo.CartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kay on 2018/3/23.
 */
@Api("Carts - 购物车")
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final AuthService authService;

    @Autowired
    public CartController(CartService cartService, AuthService authService) {
        this.cartService = cartService;
        this.authService = authService;
    }

    @ApiOperation("Add product to charts")
    @PostMapping
    public CartVo add(@RequestParam Integer productId, @RequestParam Integer count, HttpServletRequest request) {
        return cartService.add(getUserId(request), productId, count);
    }

    /**
     * 更新数量
     *
     * @param productId
     * @param count
     * @param request
     * @return
     */
    @ApiOperation("update count")
    @GetMapping("/update_count")
    public CartVo update(Integer productId, Integer count, HttpServletRequest request) {
        return cartService.update(getUserId(request), productId, count);
    }

    /**
     * 移除产品
     *
     * @param productIds
     * @param request
     * @return
     */
    @ApiOperation("remove product from charts")
    @GetMapping("/remove_product")
    public CartVo delete(@RequestBody String productIds, HttpServletRequest request) {
        return cartService.deleteByProductIds(getUserId(request), productIds);
    }

    @ApiOperation("list")
    @GetMapping("/list")
    public CartVo list(HttpServletRequest request) {
        return cartService.list(getUserId(request));
    }

    @GetMapping("/select")
    public CartVo select(HttpServletRequest request, Integer productId) {
        return cartService.selectOrUnSelect(getUserId(request), productId, ChoiceEnum.CHECKED);
    }

    @GetMapping("/un_select")
    public CartVo unSelect(HttpServletRequest request, Integer productId) {
        return cartService.selectOrUnSelect(getUserId(request), productId, ChoiceEnum.UN_CHECKED);
    }

    @GetMapping("/select_all")
    public CartVo selectAll(HttpServletRequest request) {
        return cartService.selectOrUnSelect(getUserId(request), null, ChoiceEnum.CHECKED);
    }

    @GetMapping("/un_select_all")
    public CartVo unSelectAll(HttpServletRequest request) {
        return cartService.selectOrUnSelect(getUserId(request), null, ChoiceEnum.UN_CHECKED);
    }

    @GetMapping("/get_cart_product_count")
    public Integer getCartProductCount(HttpServletRequest request) {
        return cartService.getCartProductCount(getUserId(request));
    }

    private Integer getUserId(HttpServletRequest request) {
        return authService.getUserId(request);
    }

}
