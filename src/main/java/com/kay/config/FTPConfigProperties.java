package com.kay.config;

import lombok.Data;

@Data
public class FTPConfigProperties {
    private String serverPrefix;

    private String username;

    private String password;

    private String serverIp;
}
