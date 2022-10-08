package com.xx.config;

import com.xx.filter.AdminFilter;
import com.xx.filter.CorsFilter;
import com.xx.filter.UserFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration corsInterceptor = registry.addInterceptor(new CorsFilter());
        InterceptorRegistration userInterceptor = registry.addInterceptor(new UserFilter());
        InterceptorRegistration adminInterceptor = registry.addInterceptor(new AdminFilter());

        corsInterceptor.addPathPatterns("/**");
        userInterceptor.addPathPatterns("/user/**", "/admin/**");
        adminInterceptor.addPathPatterns("/admin/**");
    }

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**")
    //             .allowedOrigins("*")
    //             .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
    //             .maxAge(3600)
    //             .allowCredentials(true);
    // }
}
