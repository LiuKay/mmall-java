package com.github.kay.mmall.infrasucture.common;

import java.util.function.Supplier;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException create(String message){
        return new ResourceNotFoundException(message);
    }

    public static Supplier<ResourceNotFoundException> supplier(String message){
        return () -> new ResourceNotFoundException(message);
    }
}
