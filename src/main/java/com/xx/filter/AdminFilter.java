package com.xx.filter;

import com.xx.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            HttpSession session = request.getSession();

            Boolean identity = ((User) session.getAttribute("USER_SESSION")).getIdentity();
            if (identity) {
                return true;
            }
            response.sendRedirect("/handleNotPermission");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
