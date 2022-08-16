package com.github.kay.mmall.domain.product;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    Collection<Product> findAllByIdIn(Collection<Integer> ids);
}
