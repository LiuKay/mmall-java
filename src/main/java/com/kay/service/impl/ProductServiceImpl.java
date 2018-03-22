package com.kay.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kay.common.ResponseCode;
import com.kay.common.ServerResponse;
import com.kay.dao.ProductMapper;
import com.kay.pojo.Product;
import com.kay.service.IProductService;
import com.kay.util.PropertiesUtil;
import com.kay.util.TimeUtil;
import com.kay.vo.ProductDetailVo;
import com.kay.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kay on 2018/3/20.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if (product==null) {
            return ServerResponse.createByErrorMessage("更新或新增产品参数错误");
        }

        //业务，主图使用子图中的第一个图
        if (product.getSubImages()!=null) {
            String[] imageArr = product.getSubImages().split(",");
            if (imageArr.length>0) {
                product.setMainImage(imageArr[0]);
            }
        }

        //判断是更新还是新增，有产品id->更新，无产品id->新增产品
        if (product.getId()==null) {
            int insertCount = productMapper.insert(product);
            if (insertCount > 0) {
                return ServerResponse.createBySuccessMessage("添加产品成功");
            }
        }else {
            int updateCount = productMapper.updateByPrimaryKey(product);
            if (updateCount>0) {
                return ServerResponse.createBySuccessMessage("更新产品成功");
            }
        }

        return ServerResponse.createByErrorMessage("添加或更新产品失败");
    }

    /**
     * 产品上下架
     * @param productId
     * @param status
     * @return
     */
    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int updateCount = productMapper.updateByPrimaryKeySelective(product);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("修改产品状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品状态失败");
    }

    /**
     * 返回商品信息
     * @param productId
     * @return
     */
    @Override
    public ServerResponse getManageProductDetail(Integer productId) {
        if (productId==null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null) {
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }

        //对象转换，pojo->vo
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /**
     * 分页列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> list(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        PageInfo pageInfo = new PageInfo<>(productList);

        List<ProductListVo> productListVoList = new ArrayList<>();
        //返回列表对象
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        pageInfo.setList(productListVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 查询列表
     * @param productId
     * @param productName
     * @param pageNum
     *@param pageSize @return
     */
    @Override
    public ServerResponse<PageInfo> getManageSearchList(Integer productId, String productName, int pageNum, int pageSize) {

        //拼接like 条件
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectListByIdAndName(productId,productName);
        PageInfo pageInfo = new PageInfo<>(productList);

        List<ProductListVo> productListVoList = new ArrayList<>();
        //返回列表对象
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        pageInfo.setList(productListVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    /**
     * 将对象转化
     * @param product
     * @return
     */
    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        //注意图片服务器地址配置在配置文件，以后实现为热部署配置
        //使用工具类加载配置读取
        String imgHost = PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/");
        productDetailVo.setImageHost(imgHost);

        //日期转换使用joda-time
        productDetailVo.setCreateTime(TimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(TimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

}
