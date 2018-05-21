package com.kay.controller.common;

import com.kay.common.Const;
import com.kay.pojo.User;
import com.kay.util.CookieUtil;
import com.kay.util.JsonUtil;
import com.kay.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by kay on 2018/5/16.
 * 重置session过期时间
 */
public class SessionExpireFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 判断用户是否登录，登录则重置redis里的session时间
     * 1.读取cookie中的loginToken
     * 2.token判空，从redis中获取user信息
     * 3.user判空，刷新redis中对于key的过期时间
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userStr = RedisPoolUtil.get(loginToken);
            User user = JsonUtil.string2obj(userStr, User.class);
            if (user != null) {
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
