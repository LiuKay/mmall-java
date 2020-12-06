package com.kay.controller.backend;

import com.kay.domain.Category;
import com.kay.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kay on 2018/3/20.
 */
@RestController
@RequestMapping("/manage/categories")
public class CategoryManageController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getCategoryChildren(
            @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return categoryService.getChildrenParallelCategory(categoryId);
    }

    /**
     * 添加节点
     *
     * @return
     */
    @PostMapping
    public Category addCategory(@RequestParam String categoryName,
                                @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        return categoryService.addCategory(categoryName, parentId);
    }

    /**
     * 修改节点名称
     */
    @PutMapping(value = "/set_category_name")
    public void setCategoryName(String categoryName, Integer categoryId) {
        categoryService.update(categoryName, categoryId);
    }

    /**
     * 遍历当前节点所有子节点
     *
     * @param categoryId
     * @return
     */
    @GetMapping(value = "/get_deep_category")
    public List<Integer> getAllCategoryChildrenById(
            @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return categoryService.selectCategoryAndChildrenById(categoryId);

    }
}
