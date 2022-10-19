package com.github.kay.mmall.warehouse.controller;

import com.github.kay.mmall.warehouse.domain.Advertisement;
import com.github.kay.mmall.warehouse.domain.AdvertisementRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restful/advertisements")
public class AdvertisementController {

    private final AdvertisementRepository repository;

    public AdvertisementController(AdvertisementRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Cacheable("resource.advertisements")
    public Iterable<Advertisement> getAllAdvertisements() {
        return repository.findAll();
    }
}
