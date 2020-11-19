package com.kay.filter;

import java.io.IOException;
import java.text.DecimalFormat;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter("/*")
public class WebRequestStatsFilter implements Filter {

    private static final DecimalFormat FORMAT = new DecimalFormat("#.00");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        long startTime = System.nanoTime();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        try {
            log.info("Start processing request: method={}, uri={}, query={}",
                     httpServletRequest.getMethod(), httpServletRequest.getRequestURI(),
                     httpServletRequest.getQueryString()
            );
            chain.doFilter(request, response);
        } finally {
            double time = (System.nanoTime() - startTime) / 1000000.0;
            log.info("{}: {} - finished in {} ms",
                     httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), FORMAT.format(time)
            );
        }
    }
}
