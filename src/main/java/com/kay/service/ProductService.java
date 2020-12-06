package com.kay.service;

import com.github.pagehelper.PageInfo;
import com.kay.domain.Product;
import com.kay.domain.ProductStatusEnum;
import com.kay.vo.ProductDetailVo;

/**
 * Created by kay on 2018/3/20.
 */
public interface ProductService {
    void saveOrUpdateProduct(Product product);

    void setSaleStatus(Integer productId, ProductStatusEnum status);

    ProductDetailVo getManageProductDetail(Integer productId);

    PageInfo getManageProductList(int pageNum, int pageSize);

    PageInfo getManageSearchList(Integer productId, String productName, int pageNum, int pageSize);

    ProductDetailVo getProductList(Integer productId);

    PageInfo getProductByKeywordCategory(Integer categoryId, String keyword, int pageNum, int pageSize,
                                         String orderBy);
}
