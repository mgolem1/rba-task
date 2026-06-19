package com.rba.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot 4 migrated its auto-configured ObjectMapper to Jackson 3
 * (tools.jackson), so the Jackson 2 (com.fasterxml.jackson) ObjectMapper used
 * throughout this project is no longer provided as a bean automatically.
 * This configuration exposes one, configured consistently with JsonSimpleHelper.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
