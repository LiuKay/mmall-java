package com.kay.controller.backend;

import com.kay.common.Const;
import com.kay.common.ResponseCode;
import com.kay.common.ServerResponse;
import com.kay.pojo.User;
import com.kay.service.ICategoryService;
import com.kay.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by kay on 2018/3/20.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "get_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCategoryChildren(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        //判断是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getCategoryChildrenByParentId(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，没有操作权限");
        }
    }

    /**
     * 添加节点
     * @param categoryName
     * @param parentId
     * @param session
     * @return
     */
    @RequestMapping(value = "add_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId",defaultValue = "0") Integer parentId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        //判断是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //添加品类
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，没有操作权限");
        }
    }

    /**
     * 修改节点名称
     * @param categoryName
     * @param categoryId
     * @param session
     * @return
     */
    @RequestMapping(value = "set_category_name.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse setCategoryName(String categoryName, Integer categoryId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        //判断是否为管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.setCategory(categoryName, categoryId);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，没有操作权限");
        }
    }

    /**
     * 遍历当前节点所有子节点
     * @param categoryId
     * @param session
     * @return
     */
    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getAllCategoryChildrenById(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getAllCategoryChildrenByParentId(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，没有操作权限");
        }
    }
}
