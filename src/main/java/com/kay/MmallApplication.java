package com.kay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author kay
 * @date 2019/8/23 21:50
 */
@SpringBootApplication
@EnableRedisHttpSession
@MapperScan("com.kay.dao")
public class MmallApplication {
    public static void main(String[] args) {
        SpringApplication.run(MmallApplication.class, args);
    }
}
