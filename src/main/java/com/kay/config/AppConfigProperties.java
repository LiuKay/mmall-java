package com.kay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app-config")
public class AppConfigProperties {

    private FTPConfigProperties ftp;

    private String alipayCallbackUrl;

    private String redisLockTime;
}
