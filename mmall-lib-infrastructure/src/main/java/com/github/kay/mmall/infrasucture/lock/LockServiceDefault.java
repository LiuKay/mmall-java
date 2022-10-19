package com.github.kay.mmall.infrasucture.lock;

import java.util.concurrent.Callable;

import lombok.SneakyThrows;

/**
 * No lock execute
 */
public class LockServiceDefault implements LockService {

    @SneakyThrows
    @Override
    public <T> T execute(String id, Callable<T> callable) {
        return callable.call();
    }

    @Override
    public void execute(String id, Runnable runnable) {
        runnable.run();
    }
}
