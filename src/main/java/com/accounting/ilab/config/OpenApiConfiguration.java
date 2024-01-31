package com.accounting.ilab.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "ILAB Accounting Service", version = "1.0"))
public class OpenApiConfiguration {

    private static final String HEADER = "header";

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> operation
                .addParametersItem(
                        new Parameter().in(HEADER)
                                .required(true)
                                .description("Basic Auth")
                                .name("Authorization")
                                .example("Bearer ZXhhbXBsZTpleGFtcGxl"));

    }
}