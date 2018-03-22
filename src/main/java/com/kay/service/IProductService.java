package com.kay.service;

import com.kay.common.ServerResponse;
import com.kay.pojo.Product;

/**
 * Created by kay on 2018/3/20.
 */
public interface IProductService {
    ServerResponse<String> saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse getManageProductDetail(Integer productId);

    ServerResponse list(int pageNum, int pageSize);

    ServerResponse getManageSearchList(Integer productId, String productName, int pageNum, int pageSize);
}
