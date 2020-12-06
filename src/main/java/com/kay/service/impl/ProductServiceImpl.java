package com.kay.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kay.dao.CategoryMapper;
import com.kay.dao.ProductMapper;
import com.kay.domain.Category;
import com.kay.domain.Product;
import com.kay.domain.ProductStatusEnum;
import com.kay.exception.InvalidOperationException;
import com.kay.exception.NotFoundException;
import com.kay.service.CategoryService;
import com.kay.service.ProductService;
import com.kay.util.DateTimeUtil;
import com.kay.util.PropertiesUtil;
import com.kay.vo.ProductDetailVo;
import com.kay.vo.ProductListVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by kay on 2018/3/20.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void saveOrUpdateProduct(Product product) {
        if (product == null) {
            return;
        }

        //业务，主图使用子图中的第一个图
        if (product.getSubImages() != null) {
            String[] imageArr = product.getSubImages().split(",");
            if (imageArr.length > 0) {
                product.setMainImage(imageArr[0]);
            }
        }

        //判断是更新还是新增，有产品id->更新，无产品id->新增产品
        if (product.getId() == null) {
            productMapper.insert(product);
        } else {
            productMapper.updateByPrimaryKey(product);
        }
    }

    /**
     * 产品上下架
     *
     * @param productId
     * @param status
     */
    @Override
    public void setSaleStatus(Integer productId, ProductStatusEnum status) {
        if (productId == null || status == null) {
            throw new IllegalArgumentException("参数错误");
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status.getCode());
        productMapper.updateByPrimaryKeySelective(product);
    }

    /**
     * 返回商品信息
     *
     * @param productId
     * @return
     */
    @Override
    public ProductDetailVo getManageProductDetail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        return assembleProductDetailVo(product);
    }

    /**
     * 分页列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo getManageProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        PageInfo pageInfo = new PageInfo<>(productList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        //返回列表对象
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        pageInfo.setList(productListVoList);
        return pageInfo;
    }

    /**
     * 查询列表
     *
     * @param productId
     * @param productName
     * @param pageNum
     * @param pageSize    @return
     * @return
     */
    @Override
    public PageInfo getManageSearchList(Integer productId, String productName, int pageNum,
                                        int pageSize) {

        //拼接like 条件
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectListByIdAndName(productId, productName);
        PageInfo pageInfo = new PageInfo<>(productList);

        List<ProductListVo> productListVoList = new ArrayList<>();
        //返回列表对象
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        pageInfo.setList(productListVoList);

        return pageInfo;
    }

    /**
     * 前台商品查询
     *
     * @param productId
     * @return
     */
    @Override
    public ProductDetailVo getProductList(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            throw new NotFoundException(String.format("Not found product for id:%s", productId));
        }

        if (product.getStatus() != ProductStatusEnum.ON_SALE.getCode()) {
            throw new InvalidOperationException("产品已下架或者删除");
        }
        //对象转换，pojo->vo
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return productDetailVo;
    }

    /**
     * 商品的关键字搜索和动态排序
     *
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public PageInfo getProductByKeywordCategory(Integer categoryId, String keyword, int pageNum,
                                                int pageSize, String orderBy) {
        //参数不合法
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            throw new IllegalArgumentException("keyword or categoryId can not be null.");
        }

        //A.处理分类
        List<Integer> categoryIdList = new ArrayList<>();
        if (categoryId != null) {
            //1.判断是否有这个分类
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            //2.如果不存在改分类直接返回一个空的集合
            if (category == null) {
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return pageInfo;
            }
            //3.若存在分类，把categoryId当作一个大类,取出所有的节点 , 在具体查询该节点内的条件，故改list先提取出来
            categoryIdList = categoryService.selectCategoryAndChildrenById(categoryId);
        }

        //B.处理模糊查询条件
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        //C.处理排序----排序的参数要根据接口文档或跟前端约定好
        if (StringUtils.isNotBlank(orderBy)) {
            //将排序关键字做成常量，以免非法传参
            if (PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderArr = orderBy.split("_");
                //使用PageHelper提供的排序功能，格式：price desc
                PageHelper.orderBy(orderArr[0] + " " + orderArr[1]);
            }
        }

        //开始查询
        PageHelper.startPage(pageNum, pageSize);
        keyword = StringUtils.isBlank(keyword) ? null : keyword;
        categoryIdList = categoryIdList.size() == 0 ? null : categoryIdList;
        //查询符合条件的并且为在售状态的产品
        List<Product> productList = productMapper.selectByNameAndCategoryIds(keyword, categoryIdList);

        List<ProductListVo> productListVoList = new ArrayList<>();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);

        return pageInfo;
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    /**
     * 将对象转化
     *
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
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

}
