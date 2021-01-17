package com.kay.service.impl;

import static com.github.pagehelper.page.PageMethod.startPage;

import com.github.pagehelper.PageInfo;
import com.kay.dao.AddressMapper;
import com.kay.domain.Address;
import com.kay.exception.NotFoundException;
import com.kay.service.AddressService;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Created by kay on 2018/3/26.
 * 注意横向越权的问题
 */
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Override
    public void add(Integer userId, Address address) {
        address.setUserId(userId);
        //some validation
        addressMapper.insert(address);
    }

    @Override
    public void delete(Integer userId, @NotNull Integer shippingId) {
        addressMapper.deleteByIdAndUserId(userId, shippingId);
    }

    @Override
    public void update(Integer userId, Address address) {
        address.setUserId(userId);
        addressMapper.updateByIdAndUserId(address);
    }

    @Override
    public Address get(Integer userId, @NonNull Integer shippingId) {
        Address address = addressMapper.selectByIdAndUserId(userId, shippingId);
        if (address == null) {
            throw new NotFoundException(String.format("Not found shipping for userId:%s,shippingId:%s",
                                                      userId, shippingId));
        }
        return address;
    }

    @Override
    public PageInfo<Address> list(Integer userId, int pageNum, int pageSize) {
        startPage(pageNum, pageSize);
        List<Address> addressList = addressMapper.selectByUserId(userId);
        return new PageInfo<>(addressList);
    }

}
