package com.xx.filter;

import com.xx.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            HttpSession session = request.getSession();

            User user = (User) session.getAttribute("USER_SESSION");
            if (user != null) {
                return true;
            }
            response.sendRedirect("/handleNotLogin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
