package com.kay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * http://localhost:8081/swagger-ui.html#/
 *
 * @author kay
 * @date 2019/9/1 20:22
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * 对所有api扫描配置:controller路径
     */
    private static final String BASE_PACKAGE = "com.kay.controller";

    /**
     * Swagger2的配置文件：内容、扫描包等
     *
     * @return the docket
     */
    @Bean
    public Docket createApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public RequestMappingInfoHandlerMapping handlerMappings() {
        return new RequestMappingHandlerMapping();
    }

    /**
     * 构建api文档的详细方法
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Mmall 测试使用 Swagger2 构建Api")
                //创建人
                .contact(new Contact("name", "url", "email"))
                //版本号
                .version("1.0.0")
                //描述
                .description("API 描述")
                .build();
    }
}
