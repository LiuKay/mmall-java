package com.github.kay.mmall.warehouse.controller;

import com.github.kay.mmall.domain.security.Role;
import com.github.kay.mmall.domain.product.Stockpile;
import com.github.kay.mmall.domain.product.Product;
import com.github.kay.mmall.infrasucture.common.CodedMessage;
import com.github.kay.mmall.infrasucture.common.CommonResponse;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/restful/products")
@CacheConfig(cacheNames = "resource.product")
public class ProductController {

    private final ProductApplicationService service;

    public ProductController(ProductApplicationService productApplicationService) {
        this.service = productApplicationService;
    }

    @GetMapping
    @Cacheable(key = "'ALL_PRODUCT'")
    public Iterable<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    public Product getProduct(@PathVariable("id") Integer id) {
        return service.getProduct(id);
    }

    @PutMapping
    @Caching(evict = {
            @CacheEvict(key = "#product.id"),
            @CacheEvict(key = "'ALL_PRODUCT'")
    })
    @Secured(Role.ADMIN)
    public ResponseEntity<CodedMessage> updateProduct(@RequestBody Product product) {
        return CommonResponse.op(() -> service.saveProduct(product));
    }

    @PostMapping
    @Caching(evict = {
            @CacheEvict(key = "#product.id"),
            @CacheEvict(key = "'ALL_PRODUCT'")
    })
    @Secured(Role.ADMIN)
    public Product createProduct(@RequestBody Product product) {
        return service.saveProduct(product);
    }

    @DeleteMapping("/{id}")
    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(key = "'ALL_PRODUCT'")
    })
    @Secured(Role.ADMIN)
    public ResponseEntity<CodedMessage> removeProduct(@PathVariable("id") Integer id) {
        return CommonResponse.op(() -> service.removeProduct(id));
    }

    /**
     * 将指定的产品库存调整为指定数额
     */
    @PatchMapping("/stockpile/{productId}")
    @Secured(Role.ADMIN)
    public ResponseEntity<CodedMessage> updateStockpile(@PathVariable("productId") Integer productId, @RequestParam("amount") Integer amount) {
        return CommonResponse.op(() -> service.setStockpileAmountByProductId(productId, amount));
    }

    @GetMapping("/stockpile/{productId}")
    @Secured(Role.ADMIN)
    public Stockpile queryStockpile(@PathVariable("productId") Integer productId) {
        return service.getStockpile(productId);
    }
    
}
