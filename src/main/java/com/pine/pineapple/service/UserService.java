package com.pine.pineapple.service;

import com.pine.pineapple.entity.User;
import com.pine.pineapple.entity.VO.UserVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User findByUsername(String username);
    User getById(Long id);
    UserVO register(User user);
    UserVO login(String username, String password);
    void resetPassword(Long id,String oldPassword,String newPassword);
}

