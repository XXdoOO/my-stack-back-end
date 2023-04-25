package com.xx.util;

import com.xx.pojo.vo.UserVO;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserInfoUtils {

    public static UserVO getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.equals("anonymousUser")) {
            return new UserVO();
        }
        return (UserVO) principal;
    }

    public static Long getId() {
        return ((UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }
}
