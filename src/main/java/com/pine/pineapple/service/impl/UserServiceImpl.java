package com.pine.pineapple.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pine.pineapple.common.utils.JwtUtil;
import com.pine.pineapple.entity.User;
import com.pine.pineapple.mapper.UserMapper;
import com.pine.pineapple.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Resource
    UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public void register(User user) {
        // 简单校验
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    public String login(String username, String password) {
        User u = userMapper.findByUsername(username);
        if (u == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(password, u.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return JwtUtil.generateToken(u.getId());
    }
}

