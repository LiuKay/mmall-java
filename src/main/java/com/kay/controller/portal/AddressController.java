package com.kay.controller.portal;

import com.github.pagehelper.PageInfo;
import com.kay.domain.Address;
import com.kay.service.AuthService;
import com.kay.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kay on 2018/3/26.
 */
@Api("地址管理")
@RestController
@RequestMapping("/addresses")
public class AddressController {

    private AddressService addressService;
    private final AuthService authService;

    @Autowired
    public AddressController(AddressService addressService, AuthService authService) {
        this.addressService = addressService;
        this.authService = authService;
    }

    @ApiOperation("Add an address")
    @PostMapping
    public ResponseEntity<String> add(@RequestBody Address address, HttpServletRequest request) {
        addressService.add(getUserId(request), address);
        return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS");
    }

    @ApiOperation("Delete an address")
    @DeleteMapping("/{shippingId}")
    public void delete(@PathVariable Integer shippingId, HttpServletRequest request) {
        addressService.delete(getUserId(request), shippingId);
    }

    @ApiOperation("Get an address")
    @GetMapping("/{shippingId}")
    public Address get(@PathVariable Integer shippingId, HttpServletRequest request) {
        return addressService.get(getUserId(request), shippingId);
    }

    @ApiOperation("Update an address")
    @PutMapping
    public void update(@RequestBody Address address, HttpServletRequest request) {
        addressService.update(getUserId(request), address);
    }

    @ApiOperation("List addresses")
    @GetMapping
    public PageInfo<Address> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                  HttpServletRequest request) {
        return addressService.list(getUserId(request), pageNum, pageSize);
    }

    private Integer getUserId(HttpServletRequest request) {
        return authService.getUserId(request);
    }
}
