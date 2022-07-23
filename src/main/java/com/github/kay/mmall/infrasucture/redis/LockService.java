package com.github.kay.mmall.infrasucture.redis;

import java.util.concurrent.Callable;

public interface LockService {

    <T> T execute(String id, Callable<T> callable);

    void execute(String id, Runnable runnable);

}
