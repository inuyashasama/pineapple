package com.pine.pineapple.service;

import com.pine.pineapple.entity.User;
import com.pine.pineapple.entity.VO.UserVO;

public interface UserService {
    User findByUsername(String username);
    User getById(Long id);
    void register(User user);
    UserVO login(String username, String password);
}

