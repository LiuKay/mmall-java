package com.github.kay.mmall.security.provider;

import com.github.kay.mmall.domain.security.AuthenticAccount;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 预验证身份认证器
 * <p>
 * 预验证是指身份已经在其他地方（第三方）确认过
 * 预验证器的目的是将第三方身份管理系统集成到具有Spring安全性的Spring应用程序中，在本项目中，用于JWT令牌过期后重刷新时的验证
 * 此时只要检查用户是否有停用、锁定、密码过期、账号过期等问题，如果没有，可根据JWT令牌的刷新过期期限，重新给客户端发放访问令牌
 */

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
