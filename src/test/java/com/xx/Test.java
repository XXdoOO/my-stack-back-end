package com.xx;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mapper.CommentsMapper;
import com.xx.pojo.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class Test {
    @Autowired
    CommentsMapper commentsMapper;

    public void test() {
        QueryWrapper<Comments> wrapper = new QueryWrapper<>();

        List<Comments> comments = commentsMapper.selectList(wrapper.
                eq("blog_id", 1).
                gt("up", 0).
                orderByDesc("up").
                last("limit " + 0 + ", " + 20));

        for (Comments comment : comments) {
            System.out.println(comment);
        }
    }
}
