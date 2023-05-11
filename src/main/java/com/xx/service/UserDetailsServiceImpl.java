package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mapper.UserMapper;
import com.xx.pojo.entity.Disable;
import com.xx.pojo.entity.User;
import com.xx.pojo.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, username));

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        } else if (!user.getEnabled()) {
            Disable disableInfo = userService.getUserDisableInfo(user.getId());

            if (disableInfo != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                throw new DisabledException("用户因 " + disableInfo.getReason() + " 已被封禁至 " + dateFormat.format(disableInfo.getEndTime()));
            }
        }
        UserVO myInfo = userMapper.getUserInfo(null, user.getId());

        List<String> authenticationList = new ArrayList<>(Collections.singletonList(myInfo.getAdmin().toString()));

        return new UserDetailsImpl(myInfo, authenticationList);
    }
}
