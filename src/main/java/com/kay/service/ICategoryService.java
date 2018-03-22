package com.kay.service;

import com.kay.common.ServerResponse;
import com.kay.pojo.Category;

import java.util.List;

/**
 * Created by kay on 2018/3/20.
 */
public interface ICategoryService {


    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    ServerResponse<String> setCategory(String categoryName, Integer categoryId);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
