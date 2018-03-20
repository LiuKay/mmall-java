package com.kay.service;

import com.kay.common.ServerResponse;

/**
 * Created by kay on 2018/3/20.
 */
public interface ICategoryService {


    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse setCategory(String categoryName, Integer categoryId);

    ServerResponse getCategoryChildrenByParentId(Integer categoryId);

    ServerResponse getAllCategoryChildrenByParentId(Integer categoryId);
}
