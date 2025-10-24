package com.pine.pineapple.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pine.pineapple.common.utils.JwtUtil;
import com.pine.pineapple.common.utils.Result;
import com.pine.pineapple.common.utils.UploadUtil;
import com.pine.pineapple.entity.User;
import com.pine.pineapple.entity.VO.UserVO;
import com.pine.pineapple.mapper.UserMapper;
import com.pine.pineapple.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
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
    public UserVO register(User user) {
        // 简单校验
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);

        UserVO result = new UserVO();
        BeanUtil.copyProperties(user, result);
        result.setToken(JwtUtil.generateToken(user.getId()));

        return result;
    }

    @Override
    public UserVO login(String username, String password) {
        User u = userMapper.findByUsername(username);
        if (u == null) {
            throw new RuntimeException("用户不存在");
        }
        UserVO result = new UserVO();
        BeanUtil.copyProperties(u, result);
        if (!passwordEncoder.matches(password, u.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        result.setToken(JwtUtil.generateToken(u.getId()));
        return result;
    }

    @Override
    public void resetPassword(Long userId,String oldPassword,String newPassword){
        if (null == userId){
            throw new RuntimeException("用户ID不可为空");
        }
        User u = getById(userId);
        if (null == u){
            throw new RuntimeException("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, u.getPassword())){
            throw new RuntimeException("密码错误");
        }
        if (null == newPassword){
            log.warn("用户ID:[{}] 密码未设置，使用默认密码123456", userId);
            newPassword = "123456";
        }
        u.setPassword(passwordEncoder.encode(newPassword));
        updateById(u);
    }
}

