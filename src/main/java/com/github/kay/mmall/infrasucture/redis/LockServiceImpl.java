package com.github.kay.mmall.infrasucture.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LockServiceImpl implements LockService {

    private static final String LOCK_PREFIX ="LOCK:";
    private static final int RELEASE_TIME_IN_MINUTE = 10;

    private final RedissonClient redissonClient;

    public LockServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public <T> T execute(String id, Callable<T> callable) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + id);

        try {
            if (lock.tryLock(0, RELEASE_TIME_IN_MINUTE, TimeUnit.MINUTES)) {
                return callable.call();
            }

            log.warn("Failed to acquire lock for:[{}]", id);
        } catch (InterruptedException e) {
            log.warn("Lock Thread InterruptedException", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            throw new LockOperationException("Failed to execute lock operation.",e);
        } finally {
            lock.unlock();
        }

        return null;
    }

    @Override
    public void execute(String id, Runnable runnable) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + id);

        try {
            if (lock.tryLock(0, RELEASE_TIME_IN_MINUTE, TimeUnit.MINUTES)) {
                runnable.run();
            }else {
                log.warn("Failed to acquire lock for:[{}]", id);
            }
        } catch (InterruptedException e) {
            log.warn("Lock Thread InterruptedException", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            throw new LockOperationException("Failed to execute lock operation.", e);
        } finally {
            lock.unlock();
        }
    }
}
