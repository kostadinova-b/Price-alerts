package com.example.pricealertssubscrption.repository.entity.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FormatMapperConfig {

    @Bean
    public ObjectMapper json() {
        return new ObjectMapper();
    }
}
