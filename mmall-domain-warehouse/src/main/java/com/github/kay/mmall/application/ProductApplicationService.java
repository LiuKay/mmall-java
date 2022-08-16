package com.github.kay.mmall.application;

import com.github.kay.mmall.domain.payment.Stockpile;
import com.github.kay.mmall.domain.payment.StockpileService;
import com.github.kay.mmall.domain.product.Product;
import com.github.kay.mmall.domain.product.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductApplicationService {

    private final ProductService service;
    private final StockpileService stockpileService;

    public ProductApplicationService(ProductService service,
                                     StockpileService stockpileService) {
        this.service = service;
        this.stockpileService = stockpileService;
    }

    public Iterable<Product> getAllProducts() {
        return service.getAllProducts();
    }

    public Product getProduct(Integer id) {
        return service.getProduct(id);
    }

    public Product saveProduct(Product product) {
        return service.saveProduct(product);
    }

    public void removeProduct(Integer id) {
        service.removeProduct(id);
    }


    public Stockpile getStockpile(Integer productId) {
        return stockpileService.getByProductId(productId);
    }

    public void setStockpileAmountByProductId(Integer productId, Integer amount) {
        stockpileService.set(productId, amount);
    }
}
