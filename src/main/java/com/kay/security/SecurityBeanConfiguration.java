package com.kay.security;

import com.kay.dao.UserMapper;
import com.kay.security.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author LiuKay
 * @since 2019/11/27
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityBeanConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MmallPasswordEncoder();
    }

    @Bean("mmallUserDetailService")
    public UserDetailsService userDetailsService(UserMapper userMapper) {
        return new MmallUserDetailService(userMapper);
    }

}


