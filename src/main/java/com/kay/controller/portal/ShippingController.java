package com.kay.controller.portal;

import com.kay.common.ResponseCode;
import com.kay.common.ServerResponse;
import com.kay.pojo.Shipping;
import com.kay.pojo.User;
import com.kay.service.IShippingService;
import com.kay.util.CookieUtil;
import com.kay.util.JsonUtil;
import com.kay.util.RedisShardedPoolUtil;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kay on 2018/3/26.
 */
@RestController("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @GetMapping("add.do")
    public ServerResponse add(Shipping shipping, HttpServletRequest request){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @GetMapping("del.do")
    public ServerResponse delete(Integer shippingId, HttpServletRequest request){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iShippingService.delete(user.getId(), shippingId);
    }

    @GetMapping("select.do")
    public ServerResponse select(Integer shippingId, HttpServletRequest request){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    @GetMapping("update.do")
    public ServerResponse update(Shipping shipping, HttpServletRequest request){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iShippingService.update(user.getId(), shipping);
    }

    @GetMapping("list.do")
    public ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                               HttpServletRequest request){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user = JsonUtil.string2obj(RedisShardedPoolUtil.get(loginToken), User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }
}
