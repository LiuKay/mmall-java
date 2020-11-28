package com.kay.security.authentication;

import com.kay.dao.UserMapper;
import com.kay.security.authentication.jwt.JwtTokenFilter;
import com.kay.security.authentication.jwt.JwtTokenProvider;
import com.kay.security.authentication.login.UserLoginAuthenticationProvider;
import com.kay.security.authentication.mobile.MobileLoginSecurityConfig;
import com.kay.security.properties.SecurityProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.kay.security.properties.SecurityConstants.LOGIN_FORM_PROCESSING_URL;
import static com.kay.security.properties.SecurityConstants.LOGIN_MOBILE_PROCESSING_URL;
import static com.kay.security.properties.SecurityConstants.VERIFICATION_CODE_URL;

@Configuration
@EnableWebSecurity
public class BrowserSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    @Qualifier("authSuccessHandler")
    private AuthSuccessHandler authSuccessHandler;

    @Autowired
    @Qualifier("authFailureHandler")
    private AuthFailureHandler authFailureHandler;

    @Autowired
    private MobileLoginSecurityConfig mobileLoginSecurityConfig;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Apply JWT
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider, authFailureHandler);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);

        // apply mobile login
        http.apply(mobileLoginSecurityConfig);

        http.authenticationProvider(new UserLoginAuthenticationProvider(userMapper, passwordEncoder))
                .formLogin()
                .loginProcessingUrl(LOGIN_FORM_PROCESSING_URL)
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .and()
                .authorizeRequests()
                .antMatchers(
                        securityProperties.getBrowser().getLoginPage(),
                        LOGIN_MOBILE_PROCESSING_URL,
                        VERIFICATION_CODE_URL)
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow swagger to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs")//
                .antMatchers("/swagger-resources/**")//
                .antMatchers("/swagger-ui.html")//
                .antMatchers("/configuration/**")//
                .antMatchers("/webjars/**")//
                .antMatchers("/public")

                // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
                .and()
                .ignoring()
                .antMatchers("/h2-console/**/**");
    }

}
