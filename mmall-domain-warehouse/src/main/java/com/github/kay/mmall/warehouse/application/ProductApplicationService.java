package com.github.kay.mmall.warehouse.application;

import com.github.kay.mmall.domain.product.DeliveredStatus;
import com.github.kay.mmall.domain.product.Product;
import com.github.kay.mmall.domain.product.Stockpile;
import com.github.kay.mmall.warehouse.domain.ProductService;
import com.github.kay.mmall.warehouse.domain.StockpileService;

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

    /**
     * 调整商品出库状态
     */
    public void setDeliveredStatus(Integer productId, DeliveredStatus status, Integer amount) {
        switch (status) {
            case DECREASE:
                stockpileService.decrease(productId, amount);
                break;
            case INCREASE:
                stockpileService.increase(productId, amount);
                break;
            case FROZEN:
                stockpileService.frozen(productId, amount);
                break;
            case THAWED:
                stockpileService.thawed(productId, amount);
                break;
        }
    }
}
