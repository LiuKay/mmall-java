package com.kay.common;

import com.kay.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by kay on 2018/5/13.
 * 对JedisPool的封装
 */
public class RedisPool {

    private static JedisPool pool;

    private static String host = PropertiesUtil.getProperty("redis.host");

    private static int ip = Integer.parseInt(PropertiesUtil.getProperty("redis.ip"));

    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.maxTotal", "20"));

    //最大空闲连接数
    private static Integer maxIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.maxIdle", "10"));

    //最小空闲连接数
    private static Integer minIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.minIdle", "2"));

    //从连接池拿出时是否进行验证，true-验证，取出的redis连接一定可用
    private static Boolean  testOnborrow=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.testOnBorrow", "true"));

    //放回连接池时是否进行验证，true-验证，放回的redis连接一定可用
    private static Boolean testOnReturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.testOnReturn", "false"));

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setBlockWhenExhausted(true); //资源耗尽时是否阻塞
        config.setTestOnBorrow(testOnborrow);
        config.setTestOnReturn(testOnReturn);

        pool = new JedisPool(config, host, ip, 1000 * 2);
    }

    static {
        initPool();
    }


    public  static Jedis getJedis(){
        return pool.getResource();
    }

    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }

}
