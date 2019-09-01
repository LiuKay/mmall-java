package com.kay.common;

import com.kay.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kay on 2018/5/21.
 * 分布式Redis分片解决方案
 */
public class RedisShardedPool {

    private static ShardedJedisPool pool;

    private static String host1 = PropertiesUtil.getProperty("redis1.host");

    private static int port1 = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String host2 = PropertiesUtil.getProperty("redis2.host");

    private static int port2 = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

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

        JedisShardInfo info1 = new JedisShardInfo(host1, port1);  //超时时间默认是2s
        JedisShardInfo info2 = new JedisShardInfo(host2, port2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        //Hashing.MURMUR_HASH 一致性算法分片，Sharded中有默认分配虚拟节点策略
        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
    }


    public  static ShardedJedis getJedis(){
        return pool.getResource();
    }

    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();
        for (int i=0;i<10;i++) {
            jedis.set("key_" + i, "value_" + i);
        }

        returnResource(jedis);

        System.out.println("--end");
    }
}
