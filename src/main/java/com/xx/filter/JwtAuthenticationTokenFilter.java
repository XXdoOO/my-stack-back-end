package com.xx.filter;

import com.xx.pojo.vo.UserVO;
import com.xx.service.UserDetailsImpl;
import com.xx.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String token = request.getHeader("token");

        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = tokenUtil.getClaimsFromToken(token);

        String userId = claims.get("id").toString();

        if (!token.equals(redisTemplate.opsForValue().get("token-" + userId))) {
            return;
        }
        UserVO user = (UserVO) redisTemplate.opsForValue().get("user-" + userId);

        if (ObjectUtils.isEmpty(user)) {
            throw new AuthenticationException("没有用户信息");
        }
        List<SimpleGrantedAuthority> permissions = new ArrayList<>(Collections.singleton(user.getAdmin().toString()))
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), permissions);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
