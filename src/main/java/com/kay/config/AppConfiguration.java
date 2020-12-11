package com.kay.config;

import com.kay.util.FTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Autowired
    private AppConfigProperties properties;

    @Bean
    public FTPService ftpService() {
        AppConfigProperties.FTPConfigProperties ftp = properties.getFtp();
        return new FTPService(ftp.getServer(), ftp.getPort(),
                              ftp.getUsername(), ftp.getPassword());
    }

}
