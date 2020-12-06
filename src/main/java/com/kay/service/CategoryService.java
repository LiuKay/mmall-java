package com.kay.service;

import com.kay.domain.Category;

import java.util.List;

/**
 * Created by kay on 2018/3/20.
 */
public interface CategoryService {

    Category addCategory(String categoryName, Integer parentId);

    void update(String categoryName, Integer categoryId);

    List<Category> getChildrenParallelCategory(Integer categoryId);

    List<Integer> selectCategoryAndChildrenById(Integer categoryId);
}
