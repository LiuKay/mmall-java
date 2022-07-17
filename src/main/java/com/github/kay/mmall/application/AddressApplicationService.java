package com.github.kay.mmall.application;

import com.github.kay.mmall.domain.account.Address;
import com.github.kay.mmall.domain.account.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AddressApplicationService {

    private final AddressRepository addressRepository;

    public AddressApplicationService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Collection<Address> listAddresses(Integer userId) {
        return addressRepository.findAddressesByUserId(userId);
    }

    public void createAddress(Address address) {
        addressRepository.save(address);
    }

    public void updateAddress(Address address) {
        addressRepository.save(address);
    }
}
