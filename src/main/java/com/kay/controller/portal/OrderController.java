package com.kay.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.kay.common.Const;
import com.kay.common.ResponseCode;
import com.kay.common.ServerResponse;
import com.kay.pojo.User;
import com.kay.service.IOrderService;
import com.kay.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by kay on 2018/3/27.
 */
@Controller
@RequestMapping("/order/")
@Slf4j
public class OrderController {


   // private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    
    @Autowired
    private IOrderService iOrderService;


    /**
     * 创建订单
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }

        return iOrderService.createOrder(user.getId(), shippingId);
    }

    /**
     * 取消订单
     * @param orderNo
     * @param session
     * @return
     */
    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse cancel(Long orderNo,HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iOrderService.cancleOrder(user.getId(), orderNo);
    }

    //获取购物车中已经选中的商品详情
    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> getOrderDetail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iOrderService.getOrderDetail(user.getId(),orderNo);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> getOrderList(HttpSession session,
                                                 @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                 @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }
        return iOrderService.getOrderList(user.getId(),pageNum,pageSize);
    }



    /**
     * 支付
     * @param orderNo
     * @param session
     * @param request
     * @return
     */
    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(Long orderNo,HttpSession session, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }

        String path = request.getSession().getServletContext().getRealPath("upload");

        return iOrderService.pay(user.getId(), orderNo, path);
    }






    /**
     * 支付宝回调处理接口
     * @param request
     * @return
     */
    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String, String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for (Iterator iterator =requestParams.keySet().iterator();iterator.hasNext();) {
            String name = (String) iterator.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr="";
            for (int i = 0; i < values.length; i++) {
                //拼接 valueStr,用逗号拼接，最后一次不加逗号
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);
        }
        log.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params.toString());

        // todo 根据官方文档验证签名

        //todo 非常重要：根据官方文档需要移除 sign ,sign_type 两个参数,但支付宝提供的源码里只移除了sign,故需要我们自己移除 sign_type，否则付款成功却验签会失败
        params.remove("sign_type");

        try {
            boolean alipayRSACheckV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if(!alipayRSACheckV2){
                return ServerResponse.createByErrorMessage("非法请求，验证不通过，再恶意请求将报警找网警");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常",e);
        }

        //验证业务数据
        ServerResponse response = iOrderService.alipayCallback(params);
        if (response.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(Long orderNo,HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDescription());
        }

        ServerResponse response = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if (response.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }

        return ServerResponse.createBySuccess(false);
    }

}
