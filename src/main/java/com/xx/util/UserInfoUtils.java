package com.xx.util;

import com.xx.pojo.vo.UserVO;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserInfoUtils {

    public static UserVO getUser() {
        return (UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getId() {
        return ((UserVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }
}
