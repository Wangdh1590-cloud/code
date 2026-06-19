package com.minimall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.minimall.common.BusinessException;
import com.minimall.dto.LoginRequest;
import com.minimall.dto.RegisterRequest;
import com.minimall.dto.UserVO;
import com.minimall.entity.User;
import com.minimall.mapper.UserMapper;
import com.minimall.security.JwtTokenUtil;
import com.minimall.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserVO register(RegisterRequest request) {
        // 检查邮箱是否已注册
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, request.getEmail());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(400, "该邮箱已被注册");
        }

        // 创建用户
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole("CUSTOMER");
        userMapper.insert(user);

        // 生成token
        String token = jwtTokenUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        return UserVO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .token(token)
                .build();
    }

    @Override
    public UserVO login(LoginRequest request) {
        // 查找用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, request.getEmail());
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException(400, "邮箱或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(400, "邮箱或密码错误");
        }

        // 生成token
        String token = jwtTokenUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        return UserVO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .token(token)
                .build();
    }
}
