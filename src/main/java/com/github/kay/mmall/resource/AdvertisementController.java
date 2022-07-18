package com.github.kay.mmall.resource;

import com.github.kay.mmall.domain.product.Advertisement;
import com.github.kay.mmall.domain.product.AdvertisementRepository;
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
