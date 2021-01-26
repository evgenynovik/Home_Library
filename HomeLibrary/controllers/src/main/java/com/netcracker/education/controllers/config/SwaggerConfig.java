package com.netcracker.education.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Home Library")
                .description("Project")
                .license("No license").licenseUrl("http://unlicense.org").termsOfServiceUrl("").version("1.0")
                .contact(new Contact("Evgeny Novik", "https://t.me/Evgenetick",
                        "novikforce@gmail.com")).build();
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.netcracker.education.controllers"))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/library/**"))
                .build()
                .securitySchemes(Collections.singletonList(basicAuthScheme()))
                .securityContexts(Collections.singletonList(actuatorSecurityContext()));
    }

    private SecurityContext actuatorSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(basicAuthReference()))
                .forPaths(PathSelectors.ant("/library/**"))
                .build();
    }

    private SecurityScheme basicAuthScheme() {
        return new BasicAuth("basicAuth");
    }

    private SecurityReference basicAuthReference() {
        return new SecurityReference("basicAuth", new AuthorizationScope[0]);
    }
}
