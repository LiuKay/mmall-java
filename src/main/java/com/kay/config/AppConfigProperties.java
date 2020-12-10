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


    private FTPConfigProperties ftp;

    private JwtConfig jwt;

    private String alipayCallbackUrl;

    private String redisLockTime;

    @Data
    public static class FTPConfigProperties {
        private String serverPrefix;
        private String username;
        private String password;
        private String serverIp;
    }

    @Data
    public static class JwtConfig{
        private String secretKey="secret-key";
        private long validityInMilliseconds = 3600000; // 1h
    }
}
