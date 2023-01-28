package com.xx.util;

import com.xx.pojo.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class SessionUtil {
    public static User getUser() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        Object session = Objects.requireNonNull(requestAttributes).getRequest().getSession().getAttribute(
                "USER_SESSION");
        return session == null ? new User() : (User) session;
    }
}
