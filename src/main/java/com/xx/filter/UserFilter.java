package com.xx.filter;

import com.xx.pojo.entity.User;
import com.xx.pojo.vo.UserVO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            HttpSession session = request.getSession();

            if (session.getAttribute("USER_SESSION") != null) {
                return true;
            }
            response.sendRedirect("/handleNotLogin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
