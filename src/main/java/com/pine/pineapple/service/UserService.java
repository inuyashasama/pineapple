package com.pine.pineapple.service;

import com.pine.pineapple.entity.User;

public interface UserService {
    User findByUsername(String username);
    User getById(Long id);
    void register(User user);
    String login(String username, String password);
}

