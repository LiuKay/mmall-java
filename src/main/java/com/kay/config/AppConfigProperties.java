package com.kay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app-config")
public class AppConfigProperties {

    private boolean testEnabled = false;

    private boolean includeErrorTrace = false;

    private String imgPath = "/img/";

    private FTPConfigProperties ftp;

    private JwtConfig jwt;

    private String alipayCallbackUrl;

    private int redisLockTime = 5000;

    @Data
    public static class FTPConfigProperties {
        private String server;
        private int port;
        private String username;
        private String password;
    }

    @Data
    public static class JwtConfig{
        private String secretKey;
        private long validityInMilliseconds = 3600000; // 1h
    }
}
