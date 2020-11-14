package com.kay.controller.portal;

import com.kay.common.Const;
import com.kay.common.ResponseCode;
import com.kay.common.ServerResponse;
import com.kay.domain.User;
import com.kay.service.CartService;
import com.kay.util.CookieUtil;
import com.kay.util.JsonUtil;
import com.kay.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kay on 2018/3/23.
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    /**
     * 添加产品
     * @param productId
     * @param count
     * @param request
     * @return
     */
    @GetMapping("/add")
    public ServerResponse add(Integer productId, Integer count, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.add(user.getId(), productId, count);
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
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.update(user.getId(), productId, count);
    }

    /**
     * 移除产品
     * @param productIds
     * @param request
     * @return
     */
    @GetMapping("/delete_product")
    public ServerResponse delete(String productIds,HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.deleteByProductIds(user.getId(), productIds);
    }

    @GetMapping("/list")
    public ServerResponse list(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.list(user.getId());
    }

    @GetMapping("/select")
    public ServerResponse select(HttpServletRequest request,Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
    }

    @GetMapping("/un_select")
    public ServerResponse unSelect(HttpServletRequest request,Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
    }

    @GetMapping("/select_all")
    public ServerResponse selectAll(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
    }

    @GetMapping("/un_select_all")
    public ServerResponse unSelectAll(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return cartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    @GetMapping("/get_cart_product_count")
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return cartService.getCartProductCount(user.getId());
    }

}
