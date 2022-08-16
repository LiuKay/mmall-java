package com.github.kay.mmall.warehouse.domain;

import com.github.kay.mmall.domain.product.Product;
import com.github.kay.mmall.dto.Settlement;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    /**
     * 根据结算单中货物的ID，填充货物的完整信息到结算单对象上
     */
    public void replenishProductInformation(Settlement bill) {
        List<Integer> ids = bill.getItems().stream().map(Settlement.Item::getProductId).collect(Collectors.toList());
        bill.productMap = repository.findAllByIdIn(ids).stream().collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    public Iterable<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProduct(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    public void removeProduct(Integer id) {
        repository.deleteById(id);
    }
}
