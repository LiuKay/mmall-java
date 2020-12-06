package com.kay.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.kay.dao.CategoryMapper;
import com.kay.domain.Category;
import com.kay.service.CategoryService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by kay on 2018/3/20.
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    // private Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加品类
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    @Override
    public Category addCategory(String categoryName, Integer parentId) {
        if (parentId == null && StringUtils.isBlank(categoryName)) {
            throw new IllegalArgumentException("parentId or categoryName can not be null.");
        }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        //新添加的品类状态
        category.setStatus(true);

        categoryMapper.insertSelective(category);
        return category;
    }

    @Override
    public void update(String categoryName, Integer categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    /**
     * 获取子节点（同级）
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Category> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            log.info("未找到当前分类的子节点");
        }
        return categoryList;
    }

    /**
     * 获取本节点及孩子节点的ID
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Integer> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet(); //初始化set
        getCategoryById(categorySet, categoryId);
        //只存放子节点的id
        List<Integer> categoryList = Lists.newArrayList();
        for (Category category : categorySet) {
            categoryList.add(category.getId());
        }
        return categoryList;
    }

    /**
     * @param categorySet 初始set，Category需要重写hashCode 和 equals方法
     * @param categoryId
     * @return
     */
    private Set<Category> getCategoryById(Set categorySet, Integer categoryId) {
        //添加当前节点
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        //查询子节点
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        //终止条件：集合遍历完
        for (Category item : categoryList) {
            //递归添加
            getCategoryById(categorySet, item.getId());
        }
        return categorySet;
    }

}
