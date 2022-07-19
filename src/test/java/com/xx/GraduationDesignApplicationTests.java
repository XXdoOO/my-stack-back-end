package com.xx;

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
    void contextLoads() {
        List<User> list = userMapper.selectList(null);
        list.forEach(System.out::println);
    }

}
