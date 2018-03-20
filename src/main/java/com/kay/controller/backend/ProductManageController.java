package com.kay.controller.backend;

import com.kay.common.Const;
import com.kay.common.ResponseCode;
import com.kay.common.ServerResponse;
import com.kay.pojo.Product;
import com.kay.pojo.User;
import com.kay.service.IProductService;
import com.kay.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by kay on 2018/3/20.
 * 商品管理
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    /**
     * 更新或添加产品
     * @param product
     * @param session
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse saveProduct(Product product, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);
        }else {
            return ServerResponse.createByErrorMessage("没有权限");
        }
    }

    /**
     * 修改产品状态
     * @param productId
     * @param status
     * @param session
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(Integer productId,Integer status,HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.setSaleStatus(productId,status);
        }else {
            return ServerResponse.createByErrorMessage("没有权限");
        }
    }


    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getProductDetail(Integer productId,HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.getProductDetail(productId);
        }else {
            return ServerResponse.createByErrorMessage("没有权限");
        }
    }
}
