package com.github.kay.mmall.domain;

import com.github.kay.mmall.domain.account.Address;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface AddressRepository extends CrudRepository<Address, Integer> {
    Collection<Address> findAddressesByUserId(Integer userId);
}
