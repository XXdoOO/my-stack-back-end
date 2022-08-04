package com.xx.config;

import com.xx.filter.AdminFilter;
import com.xx.filter.UserFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(new UserFilter());
        InterceptorRegistration interceptor2 = registry.addInterceptor(new AdminFilter());

        interceptor.addPathPatterns("/user/**", "/admin/**");

        interceptor2.addPathPatterns("/admin/**");
    }
}
