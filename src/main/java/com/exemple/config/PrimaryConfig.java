package com.exemple.config;

import com.exemple.dao.DaoImpl;
import com.exemple.dao.IDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PrimaryConfig {
    @Bean
    @Primary
    public IDao daoPrincipal() {
        return new DaoImpl();
    }
}
