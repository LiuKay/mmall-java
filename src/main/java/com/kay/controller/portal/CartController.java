package com.kay.controller.portal;

import com.kay.common.Const;
import com.kay.common.ResponseCode;
import com.kay.common.ServerResponse;
import com.kay.pojo.User;
import com.kay.service.ICartService;
import com.kay.util.CookieUtil;
import com.kay.util.JsonUtil;
import com.kay.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kay on 2018/3/23.
 */
@RestController("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;


    /**
     * 添加产品
     * @param productId
     * @param count
     * @param request
     * @return
     */
    @GetMapping("add.do")
    public ServerResponse add(Integer productId, Integer count, HttpServletRequest request) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iCartService.add(user.getId(), productId, count);
    }

    /**
     * 更新数量
     * @param productId
     * @param count
     * @param request
     * @return
     */
    @GetMapping("update.do")
    public ServerResponse update(Integer productId,Integer count,HttpServletRequest request){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iCartService.update(user.getId(), productId, count);
    }

    /**
     * 移除产品
     * @param productIds
     * @param request
     * @return
     */
    @GetMapping("delete_product.do")
    public ServerResponse delete(String productIds,HttpServletRequest request){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iCartService.deleteByProductIds(user.getId(), productIds);
    }

    //list.do
    @GetMapping("list.do")
    public ServerResponse list(HttpServletRequest request) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iCartService.list(user.getId());
    }


    //select.do
    @GetMapping("select.do")
    public ServerResponse select(HttpServletRequest request,Integer productId) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
    }
    //un_select.do
    @GetMapping("un_select.do")
    public ServerResponse unSelect(HttpServletRequest request,Integer productId) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }

    //select_all.do
    @GetMapping("select_all.do")
    public ServerResponse selectAll(HttpServletRequest request) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }

    //un_select_all.do
    @GetMapping("un_select_all.do")
    public ServerResponse unSelectAll(HttpServletRequest request) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }

    //get_cart_product_count.do
    @GetMapping("get_cart_product_count.do")
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }

}
