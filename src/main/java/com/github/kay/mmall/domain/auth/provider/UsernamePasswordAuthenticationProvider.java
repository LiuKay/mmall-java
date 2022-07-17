package com.github.kay.mmall.domain.auth.provider;

import com.github.kay.mmall.domain.auth.service.AuthenticAccountDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticAccountDetailsService accountDetailsService;
    private final PasswordEncoder passwordEncoder;

    public UsernamePasswordAuthenticationProvider(
            AuthenticAccountDetailsService accountDetailsService,
            PasswordEncoder passwordEncoder) {
        this.accountDetailsService = accountDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password= (String) authentication.getCredentials();

        final UserDetails user = accountDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("password is wrong.");
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
