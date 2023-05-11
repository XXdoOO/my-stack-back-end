package com.xx.util;

import com.xx.pojo.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.expiration}")
    private Integer expiration;

    public UserVO getUserVo() {
        return (UserVO) redisTemplate.opsForValue().get("user-" + UserInfoUtils.getUser().getId());
    }

    public void setUserVo(UserVO userVO) {
        redisTemplate.opsForValue().set("user-" + userVO.getId(), userVO, expiration, TimeUnit.SECONDS);
    }
}
