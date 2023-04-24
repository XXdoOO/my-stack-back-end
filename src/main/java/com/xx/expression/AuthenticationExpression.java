//package com.xx.expression;
//
//import com.xx.pojo.entity.User;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * 自定义权限校验方法
// */
//@Component
//public class AuthenticationExpression {
//
//    public boolean hasTrue(String permission) {
//
//        //从 securityContextHolder中获取用户权限
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//
//        //权限信息
//        List<String> permissions = user.getPermissions();
//
//        //判断是否有权限
//        return permissions.contains(permission);
//    }
//}
