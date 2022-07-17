package com.github.kay.mmall.domain.auth.provider;

import com.github.kay.mmall.domain.auth.AuthenticAccount;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class PreAuthenticatedAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication.getPrincipal();
            AuthenticAccount account = (AuthenticAccount) token.getPrincipal();

            if (account.isEnabled() && account.isCredentialsNonExpired() && account.isAccountNonExpired() && account.isAccountNonLocked()) {
                return new PreAuthenticatedAuthenticationToken(account, "", account.getAuthorities());
            }else {
                throw new DisabledException("Account status is illegal.");
            }
        }else {
            throw new PreAuthenticatedCredentialsNotFoundException("Pre-Authentication failed, token is invalid.");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(PreAuthenticatedAuthenticationToken.class);
    }
}
