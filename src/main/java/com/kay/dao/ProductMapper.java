package com.kay.dao;

import com.kay.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> selectListByIdAndName(@Param("productId") Integer productId,@Param("productName") String productName);

    List<Product> selectByNameAndCategoryIds(@Param("productName") String productName,@Param("categoryIdList") List<Integer> categoryIdList);

    Integer selectStockByPrimaryKey(Integer productId);
}