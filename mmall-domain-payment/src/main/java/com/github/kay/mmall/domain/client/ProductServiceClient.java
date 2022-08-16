package com.github.kay.mmall.domain.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "warehouse")
public interface ProductServiceClient {
}
