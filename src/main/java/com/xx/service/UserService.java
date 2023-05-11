package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xx.config.SystemConfig;
import com.xx.mapper.*;
import com.xx.pojo.dto.UserDTO;
import com.xx.pojo.entity.*;
import com.xx.pojo.vo.UserVO;
import com.xx.util.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DisableMapper disableMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private HttpServletRequest request;

    @Resource
    private JavaMailSender mailSender;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${jwt.expiration}")
    private Integer expiration;

    public String login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        UserVO userVO = userDetails.getUserVO();

        String ip = IpUtils.getIpAddr(request);
        String ipTerritory = AddressUtils.getRealAddressByIP(ip);
        userMapper.update(null, new LambdaUpdateWrapper<User>().
                set(User::getIp, ip).
                set(User::getIpTerritory, ipTerritory).
                eq(User::getId, userVO.getId()));

        Map<String, Object> map = new HashMap<>();
        map.put("id", userVO.getId());
        String token = jwtTokenUtil.generateToken(map);

        userVO.setToken(token);
        userVO.setIp(ip);
        userVO.setIpTerritory(ipTerritory);

        redisTemplate.opsForValue().set("user-" + userVO.getId(), userVO, expiration, TimeUnit.SECONDS);
        return token;
    }

    public UserVO getUserInfo() {
        return UserInfoUtils.getUser();
    }

    public Disable getUserDisableInfo(long id) {
        QueryWrapper<Disable> disableWrapper = new QueryWrapper<>();
        Disable disable = disableMapper.selectOne(disableWrapper.
                eq("user_id", id).
                orderByDesc("end_time").
                last("limit 1"));

        // 确认用户被封禁
        if (disable != null && disable.getEndTime().getTime() > System.currentTimeMillis()) {
            return disable;
        } else { // 用户已解封
            userMapper.update(null, new LambdaUpdateWrapper<User>().set(User::getEnabled, 1).
                    eq(User::getId, id));
            return null;
        }
    }

    public boolean isExistUser(String email) {
        return userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
    }

    public boolean register(String email, String password) {
        if (isExistUser(email)) {
            return false;
        }

        User user = new User();
        user.setEmail(email);

        user.setPassword(new BCryptPasswordEncoder().encode(password));

        String avatar = "/avatar/" + (new Random().nextInt(48) + 1) + ".jpg";
        user.setAvatar(avatar);

        if (userMapper.insert(user) == 1) {
            File workDir = new File(SystemConfig.getLocalPath() + user.getId());
            return workDir.mkdir();
        }
        return false;
    }

    public boolean logout() {
        Long id = UserInfoUtils.getId();
        String key = "user-" + id;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return Boolean.TRUE.equals(redisTemplate.delete(key));
        }
        return false;
    }

    public boolean updateInfo(UserDTO dto) {
        Long id = UserInfoUtils.getUser().getId();
        User user = new User();
        user.setId(id);
        user.setNickname(dto.getNickname());

        String url = SaveFile.saveAvatar(dto.getAvatar());
        if (url != null) {
            user.setAvatar(url);
        }
        return userMapper.updateById(user) == 1;
    }

    public boolean cancelAccount() {
        Long id = UserInfoUtils.getUser().getId();

        recordMapper.delete(new LambdaQueryWrapper<Record>().eq(Record::getCreateBy, id));
        commentMapper.delete(new LambdaQueryWrapper<Comment>().eq(Comment::getCreateBy, id));
        blogMapper.delete(new LambdaQueryWrapper<Blog>().eq(Blog::getCreateBy, id));

        return userMapper.deleteById(id) == 1;
    }

    public boolean sendRegisterCode(String email) {
        String code = sendEmailCode(email);

        if (StringUtils.isNotBlank(code)) {
            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
            return true;
        } else {
            return false;
        }
    }

    public String sendEmailCode(String email) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(email);
        message.setSubject("My Stack: 您本次用于注册的验证码是");

        String verCode = VerCodeGenerate.generateVerCode();


        message.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为: " + verCode + " ,本验证码 5 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");

        try {
            mailSender.send(message);
        } catch (MailSendException e) {
            return null;
        }
        return verCode;
    }

    public List<User> getUserList(UserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        user.setAdmin(dto.getAdmin());
        user.setEnabled(dto.getEnabled());
        user.setIp(dto.getIp());
        user.setIpTerritory(dto.getIpTerritory());

        return userMapper.selectList(new QueryWrapper<>(user).
                ge(dto.getCreateTime()[0] != null, "create_time", dto.getCreateTime()[0]).
                le(dto.getCreateTime()[1] != null, "create_time", dto.getCreateTime()[1]));
    }

    public UserVO getUserInfo(Long userId) {
        return userMapper.getUserInfo(userId, UserInfoUtils.getUser().getId());
    }

    public void disableUser(UserDTO dto) {
        if (dto.getEnabled()) {
            QueryWrapper<Disable> wrapper = new QueryWrapper<>();
            disableMapper.delete(wrapper.eq("user_id", dto.getUserId()));
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            userMapper.update(null, updateWrapper.set("is_enabled", true).
                    eq("id", dto.getUserId()));
        } else if (StringUtils.isNotBlank(dto.getReason()) && dto.getMinutes() != null && dto.getMinutes() > 0) {
            Disable disable = new Disable();

            disable.setUserId(dto.getUserId());
            disable.setReason(dto.getReason());
            disable.setEndTime(new Date(System.currentTimeMillis() + dto.getMinutes() * 60000));
            disableMapper.insert(disable);

            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            userMapper.update(null, wrapper.set("is_enabled", false).
                    eq("id", dto.getUserId()));
        }
    }

    public String getToken() {
        String token = request.getHeader("token");

        Claims claims = jwtTokenUtil.getClaimsFromToken(token);
        String newToken = jwtTokenUtil.generateToken(claims);

        UserVO user = UserInfoUtils.getUser();
        user.setToken(newToken);

        redisTemplate.opsForValue().set("user-" + user.getId(), user, expiration, TimeUnit.SECONDS);
        return newToken;
    }
}
