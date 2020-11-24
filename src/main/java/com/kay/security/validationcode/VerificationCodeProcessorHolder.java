package com.kay.security.validationcode;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiuKay
 * @since 2019/12/7
 */
@Component
public class VerificationCodeProcessorHolder {

    private final Map<String, VerificationCodeProcessor> processorMap;

    @Autowired
    public VerificationCodeProcessorHolder(Map<String, VerificationCodeProcessor> processorMap) {
        this.processorMap = processorMap;
    }

    public VerificationCodeProcessor findProcessor(String processorType) {
        String processorBeanName = processorType + VerificationCodeProcessor.class.getSimpleName();
        VerificationCodeProcessor codeProcessor = processorMap.get(processorBeanName);
        if (codeProcessor == null) {
            throw new VerificationCodeException(
                    "Could not find VerificationCodeProcessor with name:" + processorBeanName);
        }
        return codeProcessor;
    }

    public VerificationCodeProcessor findProcessor(ValidationCodeType type) {
        return findProcessor(type.name().toLowerCase());
    }

}
