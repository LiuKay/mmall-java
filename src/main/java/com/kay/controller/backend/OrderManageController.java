package com.kay.controller.backend;

import com.github.pagehelper.PageInfo;
import com.kay.common.ServerResponse;
import com.kay.service.OrderService;
import com.kay.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by kay on 2018/3/28.
 * 通过权限过滤器重构之后代码减少
 */
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public ServerResponse<PageInfo> list(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        return orderService.getManageList(pageNum, pageSize);
    }

    @GetMapping("/detail")
    public ServerResponse<OrderVo> list(Long orderNo) {
        return orderService.getManageDetail(orderNo);
    }


    /**
     * 后台搜索，需要扩展
     */
    @GetMapping("/search")
    public ServerResponse<PageInfo> search(@RequestParam("orderNo") Long orderNo,
                                           @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        return orderService.getManageSearch(orderNo, pageNum, pageSize);

    }

    //发货
    @GetMapping("/send_goods")
    public ServerResponse<OrderVo> orderSendGoods(Long orderNo) {
        return orderService.getManageSendGoods(orderNo);
    }

}
