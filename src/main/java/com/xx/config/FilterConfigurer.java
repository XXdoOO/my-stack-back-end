package com.xx.config;

import com.xx.filter.AdminFilter;
import com.xx.filter.CorsFilter;
import com.xx.filter.UserFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.io.File;

@Configuration
public class FilterConfigurer implements WebMvcConfigurer {
    @Value("${cover-img.request-path}")
    private String reqPath;

    @Value("${cover-img.local-path}")
    private String locPath;

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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File logoDir = new File(locPath);
        boolean flag = false;
        if (!logoDir.exists()) {
            flag = logoDir.mkdirs();
        }
        if (flag) {
            System.out.println("已成功创建资源 logo 目录：{}" + locPath);
        }

        System.out.println("getAbsolutePath = {}" + logoDir.getAbsolutePath());
        System.out.println("getPath = {}" + logoDir.getPath());

        registry.addResourceHandler(reqPath)
                .addResourceLocations("file:" + logoDir.getAbsolutePath() + File.separator);
    }
}
