package com.kay.controller.portal;

import com.kay.common.ServerResponse;
import com.kay.service.IProductService;
import com.kay.vo.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kay on 2018/3/22.
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 产品搜索及动态排序List
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<ProductListVo> getProductList(Integer categoryId,
                                                        String keyword,
                                                        @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                        @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                                        @RequestParam(value = "orderBy",defaultValue = "") String orderBy) {
        return null;
    }

}
