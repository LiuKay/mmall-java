package com.kay.dao;

import com.kay.domain.Address;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

    int deleteByIdAndUserId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    int updateByIdAndUserId(Address address);

    Address selectByIdAndUserId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    List<Address> selectByUserId(Integer userId);
}