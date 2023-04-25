package com.xx.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xx.util.MyResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义认账异常处理
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();

        MyResponse response;
        if (e instanceof BadCredentialsException) {
            response = MyResponse.fail("账号或密码错误");
        } else {
            response = MyResponse.unauthorized("请登录后尝试");
        }

        String resBody = objectMapper.writeValueAsString(response);
        PrintWriter printWriter = httpServletResponse.getWriter();
        printWriter.print(resBody);
        printWriter.flush();
        printWriter.close();
    }
}