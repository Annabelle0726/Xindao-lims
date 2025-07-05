package com.ruoyi.framework.web.ssoAuth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.AuthenticationException;
public class SsoCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SsoCodeAuthenticationToken authenticationToken = (SsoCodeAuthenticationToken) authentication;

        String openId = (String) authenticationToken.getPrincipal();

        UserDetails userDetails = userDetailsService.loadUserByUsername(openId);

        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        SsoCodeAuthenticationToken authenticationResult = new SsoCodeAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SsoCodeAuthenticationToken 的子类或子接口
        return SsoCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
