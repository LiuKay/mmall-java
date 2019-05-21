package com.kay.controller.backend;

import com.github.pagehelper.PageInfo;
import com.kay.common.ServerResponse;
import com.kay.service.IOrderService;
import com.kay.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kay on 2018/3/28.
 * 通过权限过滤器重构之后代码减少
 */
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(
                               @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        return iOrderService.getManageList(pageNum, pageSize);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> list(Long orderNo) {
        return iOrderService.getManageDetail(orderNo);
    }


    /**
     * 后台搜索，需要扩展
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> search(@RequestParam("orderNo") Long orderNo,
                                           @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        return iOrderService.getManageSearch(orderNo,pageNum,pageSize);

    }

    //发货
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderSendGoods(Long orderNo) {
        return iOrderService.getManageSendGoods(orderNo);
    }

}
