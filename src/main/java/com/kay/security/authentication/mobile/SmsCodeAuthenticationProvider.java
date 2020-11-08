package com.kay.security.authentication.mobile;

import com.kay.security.PhoneUserDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author LiuKay
 * @since 2019/12/8
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private PhoneUserDetailService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String mobile = (String) authentication.getPrincipal();

        UserDetails userDetails = userDetailsService.loadUserByPhone(mobile);

        if (userDetails == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }

        SmsCodeAuthenticationToken result = new SmsCodeAuthenticationToken(mobile, authentication.getAuthorities());
        result.setDetails(userDetails);

        return result;
    }

    /**
     * Only supports {@link SmsCodeAuthenticationToken}
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public PhoneUserDetailService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(PhoneUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
