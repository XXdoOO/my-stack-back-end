package com.xx.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xx.filter.AdminFilter;
import com.xx.filter.CorsFilter;
import com.xx.filter.UserFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class FilterConfigurer implements WebMvcConfigurer {
    @Value("${images.request-path}")
    private String reqPath;

    @Value("${images.local-path}")
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println(locPath);
        File logoDir = new File(locPath);
        boolean flag = false;
        if (!logoDir.exists()) {
            flag = logoDir.mkdirs();
        }
        if (flag) {
            System.out.println("已成功创建资源目录：{}" + locPath);
        }

        System.out.println("getAbsolutePath = {}" + logoDir.getAbsolutePath() + File.separator);
        System.out.println("getPath = {}" + logoDir.getPath());

        registry.addResourceHandler(reqPath)
                .addResourceLocations("file:" + logoDir.getAbsolutePath() + File.separator);
    }


    public static final Map<String, HttpSession> session = new HashMap<>();

    @Bean
    public HttpSessionListener httpSessionListener() {

        return new HttpSessionListener() {

            @Override
            public void sessionCreated(HttpSessionEvent se) {

                System.out.println("sessionCreated");
                session.put(se.getSession().getId(), se.getSession());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {

                System.out.println("sessionDestroyed");
                session.remove(se.getSession().getId());
            }
        };
    }
}
