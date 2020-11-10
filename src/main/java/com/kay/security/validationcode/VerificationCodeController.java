package com.kay.security.validationcode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LiuKay
 * @since 2019/12/5
 */
@RestController
@Api(value = "验证码相关")
public class VerificationCodeController {

    private final VerificationCodeProcessorHolder processorHolder;

    @Autowired
    public VerificationCodeController(VerificationCodeProcessorHolder processorHolder) {
        this.processorHolder = processorHolder;
    }

    @GetMapping("/code/{type}")
    @ApiOperation(value = "根据 type 获取指定验证码")
    @ApiParam(name = "type", allowableValues = "sms,")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        processorHolder.findProcessor(type).create(new ServletWebRequest(request, response));
    }

}
