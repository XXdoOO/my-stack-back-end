package com.xx.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xx.util.MyResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();

        MyResponse response;

        e.printStackTrace();
        if (e instanceof BadCredentialsException) {
            response = MyResponse.fail("密码错误");
        } else if (e instanceof UsernameNotFoundException) {
            response = MyResponse.fail("账号不存在");
        } else if (e instanceof DisabledException) {
            response = MyResponse.fail(e.getMessage());
        } else if (e instanceof CredentialsExpiredException) {
            response = MyResponse.unauthorized(e.getMessage());
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