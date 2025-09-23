package com.example.bankcards.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Value("${springdoc.info.title}")
    private String springdocInfoTitle;

    @Value("${springdoc.info.version}")
    private String springdocInfoVersion;

    @Value("${springdoc.info.description}")
    private String springdocInfoDescription;

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(this.springdocInfoTitle)
                        .version(this.springdocInfoVersion)
                        .description(this.springdocInfoDescription));
    }
}
