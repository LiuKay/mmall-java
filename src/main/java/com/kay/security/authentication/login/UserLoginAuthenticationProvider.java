package com.kay.security.authentication.login;

import com.kay.dao.UserMapper;
import com.kay.domain.User;
import com.kay.vo.UserIdentityDTO;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserLoginAuthenticationProvider implements AuthenticationProvider {


    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserLoginAuthenticationProvider(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;

        String username = (String) authentication.getPrincipal();

        User user = userMapper.loadUserByUsername(username);

        if (user == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }

        additionalAuthenticationChecks(user, authenticationToken);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),
                user.getPassword(), Arrays.asList(user.getRole()));
        token.setDetails(UserIdentityDTO.fromUser(user));
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    protected void additionalAuthenticationChecks(User user,
                                                  UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() == null) {
            log.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException("Bad credentials");
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(presentedPassword, user.getPassword())) {
            log.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException("Bad credentials");
        }
    }

}
