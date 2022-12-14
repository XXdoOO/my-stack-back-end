package com.xx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.xx.mapper")
@EnableTransactionManagement
@SpringBootApplication
public class GraduationDesignApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraduationDesignApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> myWebServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/");
                factory.addErrorPages(error404Page);
            }
        };
    }
}
