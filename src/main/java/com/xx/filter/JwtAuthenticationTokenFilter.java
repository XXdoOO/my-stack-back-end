package com.xx.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xx.pojo.vo.UserVO;
import com.xx.service.UserDetailsImpl;
import com.xx.util.JwtTokenUtil;
import com.xx.util.MyResponse;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.expiration}")
    private Integer expiration;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");

        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();

        Claims claims = tokenUtil.getClaimsFromToken(token);

        if (claims == null) {
            String resBody = objectMapper.writeValueAsString(MyResponse.unauthorized("token过期"));
            PrintWriter printWriter = response.getWriter();
            printWriter.print(resBody);
            printWriter.flush();
            printWriter.close();
            return;
        }

        String userId = claims.get("id").toString();

        UserVO user = (UserVO) redisTemplate.opsForValue().get("user-" + userId);

        if (user == null || !token.equals(user.getToken())) {
            String resBody = objectMapper.writeValueAsString(MyResponse.unauthorized("token已更新"));
            PrintWriter printWriter = response.getWriter();
            printWriter.print(resBody);
            printWriter.flush();
            printWriter.close();
            return;
        }

        boolean isNeedRefresh = tokenUtil.isNeedRefresh(token);
        if (isNeedRefresh) {
            String newToken = tokenUtil.generateToken(claims);

            user.setNewToken(newToken);
            redisTemplate.opsForValue().set("user-" + userId, user, expiration, TimeUnit.SECONDS);

            response.setStatus(HttpServletResponse.SC_CREATED);
        }

        List<SimpleGrantedAuthority> permissions = new ArrayList<>(Collections.singleton(user.getAdmin().toString()))
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), permissions);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
