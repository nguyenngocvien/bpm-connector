package com.bpm.api.doc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    private static final String BASIC_AUTH_SCHEME = "basicAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("E-Connetor")
                .version("1.0")
                .description("API Connector for BPM"))
            .addSecurityItem(new SecurityRequirement().addList(BASIC_AUTH_SCHEME))
            .components(
                new io.swagger.v3.oas.models.Components()
                    .addSecuritySchemes(BASIC_AUTH_SCHEME,
                        new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("basic")
                    )
            );
    }
}
