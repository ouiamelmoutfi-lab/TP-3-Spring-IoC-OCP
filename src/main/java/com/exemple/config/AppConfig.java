package com.exemple.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"com.exemple.dao", "com.exemple.metier"})
@PropertySource("classpath:app.properties")
public class AppConfig {

}
