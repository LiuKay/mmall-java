package com.kay.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.kay.common.ChoiceEnum;
import com.kay.dao.CartMapper;
import com.kay.dao.ProductMapper;
import com.kay.domain.Cart;
import com.kay.domain.Product;
import com.kay.service.CartService;
import com.kay.util.BigDecimalUtil;
import com.kay.util.PropertiesUtil;
import com.kay.vo.CartProductVo;
import com.kay.vo.CartVo;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kay on 2018/3/23.
 */
@Service
public class CartServiceImpl implements CartService {

    private static final String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
    private static final String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public CartVo add(Integer userId, @NotNull Integer productId, @NotNull Integer count) {
        //1.判断购物车是否有该商品
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            Cart insertCart = new Cart();
            insertCart.setUserId(userId);
            insertCart.setProductId(productId);
            insertCart.setQuantity(count);
            //默认勾选
            insertCart.setChecked(ChoiceEnum.CHECKED.ordinal());
            cartMapper.insert(insertCart);
        } else {
            //已有产品，数量相加
            count = count + cart.getQuantity();
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        //2.返回VO 对象，此VO对象会多次复用，建立一个复用方法，即根据用户生成购物车VO对象
        return this.list(userId);
    }

    /**
     * 更新购物车数量
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public CartVo update(Integer userId, @NotNull Integer productId, @NotNull Integer count) {
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    /**
     * 删除---多选
     *
     * @param userId
     * @param productIds 以","分割多个id
     * @return
     */
    @Override
    public CartVo deleteByProductIds(Integer userId, @NotNull String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        cartMapper.deleteByUserIdProductIds(userId, productIdList);
        return list(userId);
    }

    @Override
    public CartVo list(Integer userId) {
        return getCartVoLimit(userId);
    }

    /**
     * 选中状态切换
     *
     * @param userId
     * @param productId
     * @param choice
     * @return
     */
    @Override
    public CartVo selectOrUnSelect(Integer userId, Integer productId, ChoiceEnum choice) {
        cartMapper.selectOrUnSelectByUserId(userId, productId, choice.ordinal());
        return this.list(userId);
    }

    @Override
    public Integer getCartProductCount(Integer userId) {
        return cartMapper.selectCartProductCount(userId);
    }

    /**
     * 购物车返回对象生成
     *
     * @param userId
     * @return
     */
    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal totalPrice = new BigDecimal("0.0");

        //取出该用户所有购物车条目Cart(userId,productId)，组装 CartProductVo--也即在购物车中的同一个商品信息
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cart : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cart.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cart.getProductId());
                cartProductVo.setProductChecked(cart.getChecked());
                cartProductVo.setProductTotalPrice(totalPrice);  //先设置一个默认值，以免product为空时计算报null

                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());//库存

                    int buyLimitCount = 0;//最终的数量
                    //判断库存是否足够
                    if (product.getStock() >= cart.getQuantity()) {
                        buyLimitCount = cart.getQuantity();
                        cartProductVo.setLimitQuantity(LIMIT_NUM_SUCCESS);  //库存足够
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(LIMIT_NUM_FAIL);  //库存不够购物车中数量，自动调整为库存
                        //检测到库存不足时，修改购物车中数量
                        Cart updateStockCart = new Cart();
                        updateStockCart.setId(cart.getId());
                        updateStockCart.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(updateStockCart);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算该商品总价，利用封装好的BigDecimalUtil工具类
                    cartProductVo.setProductTotalPrice(
                            BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                }
                //计算选中商品的购物车总价
                if (cartProductVo.getProductChecked() == ChoiceEnum.CHECKED.ordinal()) {
                    totalPrice = BigDecimalUtil
                            .add(totalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(totalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        //是否全选
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }

    /**
     * 是否全选
     *
     * @param userId
     * @return
     */
    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectUnCheckedStatusCountByUserId(userId) == 0;
    }


}
