package com.example.msgibm.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    // no specific mapping for now
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
