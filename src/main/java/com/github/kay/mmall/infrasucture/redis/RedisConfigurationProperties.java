package com.github.kay.mmall.infrasucture.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mmall.redis", ignoreUnknownFields = false)
public class RedisConfigurationProperties {

    private String url;
    private String host;
    private int port;
    private int connectionPoolSize;
    private int connectionTimeout;
    private long lockWatchdogTimeout;
    private int timeout;
    private int retryAttempts;
    private int retryInterval;
    private boolean sslEnableEndpointIdentification;


}
