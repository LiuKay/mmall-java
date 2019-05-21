package com.kay.controller.backend;

import com.kay.common.ServerResponse;
import com.kay.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kay on 2018/3/20.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "get_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCategoryChildren(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    /**
     * 添加节点
     */
    @RequestMapping(value = "add_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId",defaultValue = "0") Integer parentId) {
        return iCategoryService.addCategory(categoryName, parentId);
    }

    /**
     * 修改节点名称
     */
    @RequestMapping(value = "set_category_name.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse setCategoryName(String categoryName, Integer categoryId) {
        return iCategoryService.setCategory(categoryName, categoryId);
    }

    /**
     * 遍历当前节点所有子节点
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getAllCategoryChildrenById(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return iCategoryService.selectCategoryAndChildrenById(categoryId);

    }
}
