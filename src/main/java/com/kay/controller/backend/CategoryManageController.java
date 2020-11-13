package com.kay.controller.backend;

import com.kay.common.ServerResponse;
import com.kay.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kay on 2018/3/20.
 */
@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/get_category")
    public ServerResponse getCategoryChildren(
            @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return categoryService.getChildrenParallelCategory(categoryId);
    }

    /**
     * 添加节点
     */
    @GetMapping(value = "/add_category")
    public ServerResponse addCategory(String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        return categoryService.addCategory(categoryName, parentId);
    }

    /**
     * 修改节点名称
     */
    @GetMapping(value = "/set_category_name")
    public ServerResponse setCategoryName(String categoryName, Integer categoryId) {
        return categoryService.setCategory(categoryName, categoryId);
    }

    /**
     * 遍历当前节点所有子节点
     *
     * @param categoryId
     * @return
     */
    @GetMapping(value = "/get_deep_category")
    public ServerResponse getAllCategoryChildrenById(
            @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return categoryService.selectCategoryAndChildrenById(categoryId);

    }
}
