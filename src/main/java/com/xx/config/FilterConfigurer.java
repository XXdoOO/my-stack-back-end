package com.xx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class FilterConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File logoDir = new File(SystemConfig.getLocalPath());
        boolean flag = false;
        if (!logoDir.exists()) {
            flag = logoDir.mkdirs();
        }
        if (flag) {
            System.out.println("已成功创建资源目录：{}" + SystemConfig.getLocalPath());
        }

        registry.addResourceHandler(SystemConfig.getRequestPath())
                .addResourceLocations("file:" + logoDir.getAbsolutePath() + File.separator);
    }
}
