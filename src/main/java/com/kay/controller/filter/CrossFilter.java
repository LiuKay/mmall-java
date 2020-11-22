package com.kay.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

/**
 * Created by kay on 2018/5/24.
 */
@Component
public class CrossFilter implements Filter{

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
        /*response.setHeader("Access-Control-Max-Age","3600");*/
        response.setHeader("Access-Control-Allow-Credentials","true");
        filterChain.doFilter(servletRequest,response);
    }

}
