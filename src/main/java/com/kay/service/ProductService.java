package com.kay.service;

import com.github.pagehelper.PageInfo;
import com.kay.common.ServerResponse;
import com.kay.pojo.Product;
import com.kay.vo.ProductDetailVo;
import com.kay.vo.ProductListVo;

/**
 * Created by kay on 2018/3/20.
 */
public interface ProductService {
    ServerResponse<String> saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse getManageProductDetail(Integer productId);

    ServerResponse getManageProductList(int pageNum, int pageSize);

    ServerResponse getManageSearchList(Integer productId, String productName, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductList(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(Integer categoryId, String keyword, int pageNum, int pageSize, String orderBy);
}
