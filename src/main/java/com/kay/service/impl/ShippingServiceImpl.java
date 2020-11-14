package com.kay.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.kay.common.ResponseCode;
import com.kay.common.ServerResponse;
import com.kay.dao.ShippingMapper;
import com.kay.domain.Shipping;
import com.kay.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by kay on 2018/3/26.
 * 注意横向越权的问题
 */
@Service("iShippingService")
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int insertCount = shippingMapper.insert(shipping);
        if (insertCount > 0) {
            Map resultMap = Maps.newHashMap();
            resultMap.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccessMessage("添加地址成功", resultMap);
        }
        return ServerResponse.createByErrorMessage("添加地址失败");
    }


    /**
     * 删除：注意横向越权问题
     * @param userId
     * @param shippingId
     * @return
     */
    @Override
    public ServerResponse<String> delete(Integer userId, Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        int count = shippingMapper.deleteByIdAndUserId(userId, shippingId);
        if (count ==1) {
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        if(shipping ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        //防止横向越权
        shipping.setUserId(userId);
        int count = shippingMapper.updateByIdAndUserId(shipping);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDescription());
        }
        Shipping shipping = shippingMapper.selectByIdAndUserId(userId, shippingId);
        if (shipping != null) {
            return ServerResponse.createBySuccess(shipping);
        }

        return ServerResponse.createByErrorMessage("无法查询到该地址");
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo<>(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }


}
