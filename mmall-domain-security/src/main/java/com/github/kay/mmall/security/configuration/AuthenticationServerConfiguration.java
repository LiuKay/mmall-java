package com.github.kay.mmall.security.configuration;

import com.github.kay.mmall.security.provider.PreAuthenticatedAuthenticationProvider;
import com.github.kay.mmall.security.provider.UsernamePasswordAuthenticationProvider;
import com.github.kay.mmall.domain.auth.AuthenticAccountDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户认证服务配置
 */
@Configuration
@EnableWebSecurity
public class AuthenticationServerConfiguration extends WebSecurityConfiguration{

    private final AuthenticAccountDetailsService authenticAccountDetailsService;
    private final UsernamePasswordAuthenticationProvider userProvider;
    private final PreAuthenticatedAuthenticationProvider preProvider;
    private final PasswordEncoder encoder;

    public AuthenticationServerConfiguration(
            AuthenticAccountDetailsService authenticAccountDetailsService,
            UsernamePasswordAuthenticationProvider userProvider,
            PreAuthenticatedAuthenticationProvider preProvider,
            PasswordEncoder encoder) {
        this.authenticAccountDetailsService = authenticAccountDetailsService;
        this.userProvider = userProvider;
        this.preProvider = preProvider;
        this.encoder = encoder;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticAccountDetailsService)
            .passwordEncoder(encoder);
        auth.authenticationProvider(userProvider);
        auth.authenticationProvider(preProvider);
    }
}
