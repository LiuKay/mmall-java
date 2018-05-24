package com.kay.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by kay on 2018/5/24.
 */
public class CrossFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        String origin= servletRequest.getRemoteHost()+":"+servletRequest.getRemotePort();
        //logger.debug(" CrossFilter : remote host and port:[{}]",origin);
        // System.out.println("********************************"+origin);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
        /*response.setHeader("Access-Control-Max-Age","3600");*/
        response.setHeader("Access-Control-Allow-Credentials","true");
        filterChain.doFilter(servletRequest,response);
    }

    @Override
    public void destroy() {

    }
}
