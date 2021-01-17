package com.kay.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableKnife4j
@Configuration
public class Swagger2Config {

    private static final String BASE_PACKAGE = "com.kay.controller";

    @Bean
    public Docket createApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("Mmall商城")
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(securityContext());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Mall 测试使用 Swagger2 构建Api")
                .contact(new Contact("name", "url", "email"))
                .version("1.0.0")
                .description("API 描述")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }

    private List<SecurityContext> securityContext() {
        return Arrays.asList(SecurityContext.builder()
                                            .securityReferences(defaultAuth())
                                            .forPaths(PathSelectors.any())
                                            .build());
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        SecurityReference reference = SecurityReference.builder().reference("apiKey")
                                                       .scopes(authorizationScopes)
                                                       .build();
        return Arrays.asList(reference);
    }
}
