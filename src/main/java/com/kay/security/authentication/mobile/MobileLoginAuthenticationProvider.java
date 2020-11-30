package com.kay.security.authentication.mobile;

import com.kay.dao.UserMapper;
import com.kay.domain.User;
import com.kay.security.validationcode.VerificationCodeProcessor;
import com.kay.vo.UserIdentityDTO;
import java.util.Collections;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;

public class MobileLoginAuthenticationProvider implements AuthenticationProvider {

    private final UserMapper userMapper;

    public MobileLoginAuthenticationProvider(UserMapper userMapper,
                                             VerificationCodeProcessor verificationCodeProcessor) {
        this.userMapper = userMapper;
        this.verificationCodeProcessor = verificationCodeProcessor;
    }

    private final VerificationCodeProcessor verificationCodeProcessor;


    @Override
    public Authentication authenticate(Authentication authentication) {
        String mobile = (String) authentication.getPrincipal();
        String smsCode = (String) authentication.getCredentials();

        User user = userMapper.loadUserByPhone(mobile);

        if (user == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }

        verificationCodeProcessor.validate(mobile, smsCode);
        MobileLoginToken authenticationToken = new MobileLoginToken(user.getUsername(),
                                                                    user.getPassword(),
                                                                    Collections.singletonList(user.getRole()));
        authenticationToken.setDetails(UserIdentityDTO.fromUser(user));
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileLoginToken.class.isAssignableFrom(authentication);
    }

}
