package com.kay.common.Exception;

import com.kay.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kay on 2018/5/22.
 * 全局异常包装
 */
@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //打印异常日志
        log.error("{} Exception:",httpServletRequest.getRequestURI(),e);
        //此处因为本项目使用Jackson的包为1.9版本，若使用2.x版本要使用MappingJackson2JsonView
        ModelAndView modelAndView = new ModelAndView(new MappingJacksonJsonView());
        //返回与ServerResponse封装相同的格式
        modelAndView.addObject("status", ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg", "接口发生异常，详情请查看服务器日志");
        modelAndView.addObject("data", e.toString());
        return modelAndView;
    }
}
