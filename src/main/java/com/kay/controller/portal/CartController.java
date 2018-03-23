package com.kay.controller.portal;

import com.kay.common.Const;
import com.kay.common.ResponseCode;
import com.kay.common.ServerResponse;
import com.kay.pojo.User;
import com.kay.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by kay on 2018/3/23.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;


    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(Integer productId, Integer count, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iCartService.add(user.getId(), productId, count);
    }

}
