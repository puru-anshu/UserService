package com.arutech.mftracker.UserService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonDateTimeConfig {

    @Bean
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();

        // Custom serializer to handle potential null values
        LocalDateTimeSerializer serializer = new LocalDateTimeSerializer(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        ) {
            @Override
            public void serialize(LocalDateTime value,
                                  com.fasterxml.jackson.core.JsonGenerator gen,
                                  com.fasterxml.jackson.databind.SerializerProvider serializers)
                    throws java.io.IOException {
                if (value == null) {
                    gen.writeNull();
                } else {
                    super.serialize(value, gen, serializers);
                }
            }
        };

        module.addSerializer(LocalDateTime.class, serializer);

        return Jackson2ObjectMapperBuilder.json()
                .modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }
}
