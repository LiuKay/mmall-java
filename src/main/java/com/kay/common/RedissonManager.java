package com.kay.common;

import com.kay.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

/**
 * Created by kay on 2018/5/23.
 */
@Component
@Slf4j
public class RedissonManager {

    private Redisson redisson = null;

    private Config config = new Config();

    private static String host1 = PropertiesUtil.getProperty("redis1.host");
    private static int port1 = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String host2 = PropertiesUtil.getProperty("redis2.host");
    private static int port2 = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

//    @PostConstruct
    private void init() {
        try {
            config.useSingleServer().setAddress(new StringBuilder().append(host1).append(":").append("port1").toString());

            redisson = (Redisson) Redisson.create(config);
            log.info("Redisson 初始化完成");
        } catch (Exception e) {
           log.error("init Redisson error ",e);
        }
    }

    public Redisson getRedisson() {
        return redisson;
    }
}
