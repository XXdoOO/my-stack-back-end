package com.xx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "stack")
public class SystemConfig {
    private static String requestPath;

    private static String localPath;

    public static String getRequestPath() {
        return requestPath;
    }

    public static String getLocalPath() {
        return localPath;
    }

    public void setRequestPath(String requestPath) {
        SystemConfig.requestPath = requestPath;
    }

    public void setLocalPath(String localPath) {
        SystemConfig.localPath = localPath;
    }
}
