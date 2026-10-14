package com.exemple.config;

import com.exemple.dao.DaoApi;
import com.exemple.dao.IDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DaoAliasConfig {

    @Bean

    public IDao daoAlias(){
        return new DaoApi();
    }
}
