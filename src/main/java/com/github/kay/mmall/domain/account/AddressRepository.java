package com.github.kay.mmall.domain.account;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface AddressRepository extends CrudRepository<Address, Integer> {
    Collection<Address> findAddressesByUserId(Integer userId);
}
