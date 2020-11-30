package com.kay.service;

import com.github.pagehelper.PageInfo;
import com.kay.domain.Address;
import org.springframework.lang.NonNull;

/**
 * Created by kay on 2018/3/26.
 */
public interface AddressService {
    void add(Integer userId, Address address);

    void delete(Integer userId, @NonNull Integer shippingId);

    void update(Integer userId, Address address);

    Address get(Integer userId, @NonNull Integer shippingId);

    PageInfo<Address> list(Integer userId, int pageNum, int pageSize);

}
