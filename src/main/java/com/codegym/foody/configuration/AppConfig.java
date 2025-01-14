package com.codegym.foody.configuration;

import com.codegym.foody.formatter.DateFormatter;
import com.codegym.foody.formatter.PriceFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DateFormatter dateFormatter() {
        return new DateFormatter();
    }

    @Bean
    public PriceFormatter priceFormatter() {
        return new PriceFormatter();
    }
}
