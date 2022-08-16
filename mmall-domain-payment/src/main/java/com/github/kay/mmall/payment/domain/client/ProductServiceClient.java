package com.github.kay.mmall.payment.domain.client;

import com.github.kay.mmall.domain.product.DeliveredStatus;
import com.github.kay.mmall.domain.product.Stockpile;
import com.github.kay.mmall.domain.product.Product;
import com.github.kay.mmall.dto.Settlement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FeignClient(name = "warehouse")
public interface ProductServiceClient {

    default void replenishProductInformation(Settlement bill) {
        bill.productMap = Stream.of(getProducts()).collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    @GetMapping("/restful/products/{id}")
    Product getProduct(@PathParam("id") Integer id);

    @GetMapping("/restful/products")
    Product[] getProducts();

    default void decrease(Integer productId, Integer amount) {
        setDeliveredStatus(productId, DeliveredStatus.DECREASE, amount);
    }

    default void increase(Integer productId, Integer amount) {
        setDeliveredStatus(productId, DeliveredStatus.INCREASE, amount);
    }

    default void frozen(Integer productId, Integer amount) {
        setDeliveredStatus(productId, DeliveredStatus.FROZEN, amount);
    }

    default void thawed(Integer productId, Integer amount) {
        setDeliveredStatus(productId, DeliveredStatus.THAWED, amount);
    }

    @PatchMapping("/restful/products/stockpile/delivered/{productId}")
    void setDeliveredStatus(@PathParam("productId") Integer productId, @RequestParam("status") DeliveredStatus status, @RequestParam("amount") Integer amount);

    @GetMapping("/restful/products/stockpile/{productId}")
    Stockpile queryStockpile(@PathParam("productId") Integer productId);
}
