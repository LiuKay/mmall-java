package com.kay.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kay.dao.UserMapper;
import com.kay.security.handler.BrowserAuthenticationFailureHandler;
import com.kay.security.handler.BrowserAuthenticationSuccessHandler;
import com.kay.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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

    /**
     * Store token in DB
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //TODO: only call once to create a table, return Exception secondly. Or you can create table by yourself.
//        tokenRepository.setCreateTableOnStartup(true);
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MmallPasswordEncoder();
    }

    @Bean("mmallUserDetailService")
    public UserDetailsService userDetailsService(UserMapper userMapper) {
        return new MmallUserDetailService(userMapper);
    }

    @Bean("browserAuthenticationFailureHandler")
    public AuthenticationFailureHandler failureHandler(ObjectMapper mapper, SecurityProperties securityProperties) {
        return new BrowserAuthenticationFailureHandler(mapper, securityProperties);
    }

    @Bean("browserAuthenticationSuccessHandler")
    public AuthenticationSuccessHandler successHandler(ObjectMapper mapper, SecurityProperties securityProperties) {
        return new BrowserAuthenticationSuccessHandler(mapper, securityProperties);
    }

}


