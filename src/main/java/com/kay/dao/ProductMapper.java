package com.kay.dao;

import com.kay.domain.Product;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> selectListByIdAndName(@Param("productId") Integer productId,
                                        @Param("productName") String productName);

    List<Product> selectByNameAndCategoryIds(@Param("productName") String productName,
                                             @Param("categoryIdList") List<Integer> categoryIdList);

    Integer selectStockByPrimaryKey(Integer productId);
}