package com.github.kay.mmall.warehouse.domain;

import com.github.kay.mmall.domain.product.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    Collection<Product> findAllByIdIn(Collection<Integer> ids);
}
