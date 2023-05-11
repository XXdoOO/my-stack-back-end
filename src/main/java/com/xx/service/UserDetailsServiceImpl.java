package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xx.mapper.DisableMapper;
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
    private DisableMapper disableMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, username));

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        } else if (!user.getEnabled()) {
            Disable disable = disableMapper.selectOne(new LambdaQueryWrapper<Disable>().
                    eq(Disable::getUserId, user.getId()).
                    orderByDesc(Disable::getEndTime).
                    last("limit 1"));

            // 确认用户被封禁
            if (disable != null && disable.getEndTime().getTime() > System.currentTimeMillis()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                throw new DisabledException("用户因 " + disable.getReason() + " 已被封禁至 " + dateFormat.format(disable.getEndTime()));
            } else { // 用户已解封
                userMapper.update(null, new LambdaUpdateWrapper<User>().set(User::getEnabled, 1).
                        eq(User::getId, user.getId()));
            }
        }
        UserVO myInfo = userMapper.getUserInfo(null, user.getId());

        List<String> authenticationList = new ArrayList<>(Collections.singletonList(myInfo.getAdmin().toString()));

        return new UserDetailsImpl(myInfo, authenticationList);
    }
}
