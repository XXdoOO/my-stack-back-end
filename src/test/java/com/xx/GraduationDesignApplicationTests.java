package com.xx;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mapper.UserMapper;
import com.xx.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GraduationDesignApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void login() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        User user = userMapper.selectOne(wrapper.eq("username", "xx").eq("password", "xx"));
        System.out.println(user);
    }

    @Test
    void register(){
        User user = new User("username", "password", "null", "null", null);
        int insert = userMapper.insert(user);

        System.out.println(insert);
    }

}
