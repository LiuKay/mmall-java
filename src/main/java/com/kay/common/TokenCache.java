package com.kay.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by kay on 2018/3/19.
 * 本地缓存,使用google工具类
 */
public class TokenCache {

    public static final String TOKEN_PREFIX = "token_";

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {

                //默认数据加载，get取值为空时，默认调用该方法
                @Override
                public String load(String s) throws Exception {
                    return "null";  //后期要用返回对象进行比较，避免返回null对象，equals报错
                }
            });

    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    public static String getValue(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            logger.error("localCache get error",e);
        }
        return null;
    }
}
