package com.kay.config;

import com.kay.util.FTPService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Value("${application.ftp.username}")
    private String username;

    @Value("${application.ftp.password}")
    private String password;

    @Value("${application.ftp.ip}")
    private String ip;

    @Bean
    public FTPService ftpService() {
        return new FTPService(username, password, ip);
    }

}
