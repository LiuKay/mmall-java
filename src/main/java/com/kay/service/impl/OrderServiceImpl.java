package com.kay.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kay.common.Const;
import com.kay.common.ServerResponse;
import com.kay.dao.AddressMapper;
import com.kay.dao.CartMapper;
import com.kay.dao.OrderItemMapper;
import com.kay.dao.OrderMapper;
import com.kay.dao.PayInfoMapper;
import com.kay.dao.ProductMapper;
import com.kay.domain.Address;
import com.kay.domain.Cart;
import com.kay.domain.Order;
import com.kay.domain.OrderItem;
import com.kay.domain.OrderStatusEnum;
import com.kay.domain.PayInfo;
import com.kay.domain.PayPlatformEnum;
import com.kay.domain.PaymentTypeEnum;
import com.kay.domain.Product;
import com.kay.domain.ProductStatusEnum;
import com.kay.service.OrderService;
import com.kay.util.BigDecimalUtil;
import com.kay.util.DateTimeUtil;
import com.kay.util.PropertiesUtil;
import com.kay.vo.OrderItemVo;
import com.kay.vo.OrderProductVo;
import com.kay.vo.OrderVo;
import com.kay.vo.ShippingVo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.alipay.demo.trade.config.Configs;
//import com.alipay.demo.trade.model.ExtendParams;
//import com.alipay.demo.trade.model.GoodsDetail;
//import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
//import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
//import com.alipay.demo.trade.service.AlipayTradeService;
//import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
//import com.alipay.demo.trade.utils.ZxingUtils;

/**
 * Created by kay on 2018/3/27.
 */
@Service("iOrderService")
@Slf4j
public class OrderServiceImpl implements OrderService {

    //   private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AddressMapper addressMapper;

//    private static AlipayTradeService tradeService;

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
//                Configs.init("zfbinfo.properties");
//
//        /** 使用Configs提供的默认参数
//         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
//         */
//        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }


    /**
     * 生成订单
     *
     * @param userId     用户id
     * @param shippingId 地址id
     * @return
     */
    public ServerResponse createOrder(Integer userId, Integer shippingId) {

        //获取购物车列表(已勾选)
        List<Cart> cartList = cartMapper.selectCheckedCartListByUserId(userId);

        ServerResponse serverResponse = this.getCartOrderItemList(userId, cartList);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }

        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        if (CollectionUtils.isEmpty(orderItemList)) {
            return ServerResponse.error("购物车为空");
        }

        //计算总价
        BigDecimal payment = this.getTotalPrice(orderItemList);

        //生成订单
        //组装订单对象
        Order order = this.assembleOrder(userId, shippingId, payment);
        if (order == null) {
            return ServerResponse.error("生成订单错误");
        }

        //同一个订单的订单条目，设置订单号
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
        }

        //批量插入到order_item
        orderItemMapper.batchInsert(orderItemList);

        //减库存
        this.reduceProductStock(orderItemList);
        //清空购物车
        this.clearCart(cartList);

        //返回前端，格式转换 生成 Vo
        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);

        return ServerResponse.success(orderVo);
    }


    /**
     * 取消订单
     *
     * @param userId
     * @param orderNo
     * @return
     */
    public ServerResponse cancleOrder(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.error("该用户此订单不存在");
        }
        if (order.getStatus() != OrderStatusEnum.NO_PAY.getCode()) {
            return ServerResponse.error("已付款，无法取消订单");
        }

        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(OrderStatusEnum.CANCEL.getCode());

        int rowCount = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (rowCount > 0) {
            return ServerResponse.success();
        }

        return ServerResponse.error();
    }

    /**
     * 查询购物车中选中的商品详情
     *
     * @param userId
     * @return
     */
    @Override
    public ServerResponse getOrderCartProduct(Integer userId) {
        OrderProductVo orderProductVo = new OrderProductVo();
        //从购物车中获取数据

        List<Cart> cartList = cartMapper.selectCheckedCartListByUserId(userId);
        ServerResponse serverResponse = this.getCartOrderItemList(userId, cartList);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }

        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        BigDecimal payment = new BigDecimal("0.0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return ServerResponse.success(orderProductVo);
    }

    /**
     * 获取商品详情
     *
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.selectByUserIdOrderNo(userId, orderNo);
            OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
            return ServerResponse.success(orderVo);
        }
        return ServerResponse.error("找不到该订单");
    }

    @Override
    public ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(orderList);

        List<OrderVo> orderVoList = this.assembleOrderVoList(userId, orderList);
        pageInfo.setList(orderVoList);

        return ServerResponse.success(pageInfo);
    }


    private List<OrderVo> assembleOrderVoList(Integer userId, List<Order> orderList) {
        List<OrderVo> orderVoList = Lists.newArrayList();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = Lists.newArrayList();
            if (userId == null) {
                //todo 管理员查询
                orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
            } else {
                orderItemList = orderItemMapper.selectByUserIdOrderNo(userId, order.getOrderNo());
            }

            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }


    /**
     * 组装 OrderVo 对象
     *
     * @param order
     * @param orderItemList
     * @return
     */
    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList) {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVo.setShippingId(order.getShippingId());
        Address address = addressMapper.selectByPrimaryKey(order.getShippingId());
        if (address != null) {
            orderVo.setReceiverName(address.getReceiverName());
            orderVo.setShippingVo(assembleShippingVo(address));
        }

        orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));
        orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));

        orderVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    //订单单条Vo
    private OrderItemVo assembleOrderItemVo(OrderItem orderItem) {
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());

        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }

    //组装地址详情Vo
    private ShippingVo assembleShippingVo(Address address) {
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(address.getReceiverName());
        shippingVo.setReceiverAddress(address.getReceiverAddress());
        shippingVo.setReceiverProvince(address.getReceiverProvince());
        shippingVo.setReceiverCity(address.getReceiverCity());
        shippingVo.setReceiverDistrict(address.getReceiverDistrict());
        shippingVo.setReceiverMobile(address.getReceiverMobile());
        shippingVo.setReceiverZip(address.getReceiverZip());
        shippingVo.setReceiverPhone(shippingVo.getReceiverPhone());
        return shippingVo;
    }

    //清空购物车
    private void clearCart(List<Cart> cartList) {
        for (Cart cart : cartList) {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    //减少库存
    private void reduceProductStock(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    //组装订单对象
    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        Order order = new Order();
        long orderNo = this.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);
        order.setUserId(userId);
        order.setShippingId(shippingId);

        //todo 创建订单时还没有发货和付款等信息

        int rowCount = orderMapper.insert(order);
        if (rowCount > 0) {
            return order;
        }

        return null;
    }

    /**
     * 生成订单号
     * fixme 此处以后要扩展,采用分布式订单号生成策咯
     *
     * @return
     */
    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    private BigDecimal getTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    public ServerResponse getCartOrderItemList(Integer userId, List<Cart> cartList) {
        List<OrderItem> orderItemList = Lists.newArrayList();

        if (CollectionUtils.isEmpty(cartList)) {
            return ServerResponse.error("购物车为空");
        }

        //校验购物车的数量，包括产品的状态和数量
        for (Cart cart : cartList) {
            //一个子订单的明细
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (ProductStatusEnum.ON_SALE.getCode() != product.getStatus()) {
                return ServerResponse.error("产品" + product.getName() + "不是在售状态");
            }

            //检查库存
            if (cart.getQuantity() > product.getStock()) {
                return ServerResponse.error("产品" + product.getName() + "库存不足");
            }

            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity()));

            orderItemList.add(orderItem);
        }
        return ServerResponse.success(orderItemList);
    }


    public ServerResponse pay(Integer userId, Long orderNo, String path) {
        Map<String, String> resultMap = Maps.newHashMap();
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.error("找不到该订单");
        }
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));

        //todo 验证逻辑

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("mmall商城扫码支付,订单号:").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共支付:").append(totalAmount)
                                         .append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        //TODO 重要


        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        //取出订单明细
        List<OrderItem> orderItemList = orderItemMapper.selectByUserIdOrderNo(userId, orderNo);

        for (OrderItem orderItem : orderItemList) {
//            GoodsDetail goods = GoodsDetail.newInstance(order.getOrderNo().toString(), orderItem.getProductName(),
//                    BigDecimalUtil.add(orderItem.getCurrentUnitPrice().doubleValue(),new Double(100)).longValue(), orderItem.getQuantity());
//            goodsDetailList.add(goods);
        }


        /*// 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx小面包", 1000, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);*/


        // 创建扫码支付请求builder，设置请求参数
//        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
//                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
//                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
//                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
//                .setTimeoutExpress(timeoutExpress)
//                                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
//                .setGoodsDetailList(goodsDetailList);
//
//
//
//
//        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
//        switch (result.getTradeStatus()) {
//            case SUCCESS:
//                log.info("支付宝预下单成功: )");
//
//                AlipayTradePrecreateResponse response = result.getResponse();
//                dumpResponse(response);
//
//                File folder = new File(path);
//                if (!folder.exists()) {
//                    folder.setWritable(true);
//                    folder.mkdirs();
//                }
//
//                // 需要修改为运行机器上的路径 todo 注意加"/"
//                String qrPath = String.format(path+"/qr-%s.png",response.getOutTradeNo());
//                String qrFileName=String.format("qr-%s.png",response.getOutTradeNo());
//
//                //生成二维码
//                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
//
//
//                File targetFile = new File(path, qrFileName);
//                try {
//                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
//                } catch (IOException e) {
//                    log.error("上传二维码异常",e);
//                }
//                log.info("filePath:" + qrPath);
//                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFile.getName();
//                resultMap.put("qrUrl", qrUrl);
//                return ServerResponse.createBySuccess(resultMap);
//            case FAILED:
//                log.error("支付宝预下单失败!!!");
//                return ServerResponse.createBySuccessMessage("支付宝预下单失败!!!");
//
//            case UNKNOWN:
//                log.error("系统异常，预下单状态未知!!!");
//                return ServerResponse.createBySuccessMessage("系统异常，预下单状态未知!!!");
//
//
//            default:
//                log.error("不支持的交易状态，交易返回异常!!!");
//                return ServerResponse.createBySuccessMessage("不支持的交易状态，交易返回异常!!!");


//        }
        return null;
    }


    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                                       response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    //回调验证
    public ServerResponse alipayCallback(Map<String, String> params) {
        long orderNo = Long.parseLong(params.get("out_trade_no"));  //业务订单号
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");

        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.error("非本站订单，回调忽略");
        }
        //订单状态
        if (order.getStatus() >= OrderStatusEnum.PAID.getCode()) {
            //需要返回success
            return ServerResponse.successWithMessage("支付宝重复调用");
        }

        //回调状态
        //已支付
        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            order.setStatus(OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }

        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());

        //第三方支付平台相关
        payInfo.setPayPlatform(PayPlatformEnum.ALIPAY.getCode());  //支付宝支付
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfoMapper.insert(payInfo);

        return ServerResponse.success();
    }

    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.error("用户没该订单");
        }
        //成功的订单
        if (order.getStatus() >= OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.success();
        }
        return ServerResponse.error();
    }


    //后台服务

    /**
     * 管理员获取订单列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> getManageList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAllOrder();
        PageInfo pageInfo = new PageInfo(orderList);

        List<OrderVo> orderVoList = this.assembleOrderVoList(null, orderList);
        pageInfo.setList(orderVoList);
        return ServerResponse.success(pageInfo);
    }

    @Override
    public ServerResponse<OrderVo> getManageDetail(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
            OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
            return ServerResponse.success(orderVo);
        }
        return ServerResponse.error("订单不存在");
    }

    /**
     * fixme 搜索需要扩展，多条件查询
     *
     * @param orderNo
     * @param pageNum
     * @param pageSize @return
     */
    @Override
    public ServerResponse<PageInfo> getManageSearch(Long orderNo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {

            List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
            OrderVo orderVo = this.assembleOrderVo(order, orderItemList);

            PageInfo pageInfo = new PageInfo(Lists.newArrayList(order));
            pageInfo.setList(Lists.newArrayList(orderVo));

            return ServerResponse.success(pageInfo);
        }
        return ServerResponse.error("订单不存在");
    }

    /**
     * 发货
     *
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse getManageSendGoods(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            if (order.getStatus() == OrderStatusEnum.PAID.getCode()) {
                order.setStatus(OrderStatusEnum.SHIPPED.getCode());
                order.setSendTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);
                return ServerResponse.success("发货成功");
            }
        }
        return ServerResponse.error("订单不存在");
    }

    /**
     * 关闭在当前时间hour小时之内未支付订单
     *
     * @param hour
     */
    @Override
    public void closeOrder(int hour) {
        //获取当前时间hour之前时间
        Date closeTime = DateUtils.addHours(new Date(), -hour);
        String startTime = DateTimeUtil.dateToStr(closeTime);
        List<Order> orderList = orderMapper
                .selectOrderByStatusAndStartTime(OrderStatusEnum.NO_PAY.getCode(), startTime);
        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
            for (OrderItem orderItem : orderItemList) {

                //todo 使用写独占锁，一定要用主键where条件，防止锁表。同时必须是支持MySQL的Innodb。
                //获取到对应产品的库存
                Integer productStock = productMapper.selectStockByPrimaryKey(orderItem.getProductId());

                //对应商品已经删除等情况,不做处理
                if (productStock == null) {
                    continue;
                }

                //库存数量恢复
                Product product = new Product();
                product.setId(orderItem.getProductId());
                product.setStock(productStock + orderItem.getQuantity());
                int updateCount = productMapper.updateByPrimaryKeySelective(product);
            }
            //更新订单状态，关闭订单
            orderMapper.closeOrderCloseByOrderId(order.getId());
            log.info("关闭订单OrderNo:{}", order.getOrderNo());
        }
    }
}
