package com.pine.pineapple.controller;


import com.pine.pineapple.common.utils.Result;
import com.pine.pineapple.entity.User;
import com.pine.pineapple.entity.VO.UserVO;
import com.pine.pineapple.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody User user) {
        try {
            UserVO userVO = userService.register(user);
            return Result.ok("注册成功", userVO);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            UserVO userVO = userService.login(username, password);
            return Result.ok("登录成功", userVO);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }


    @GetMapping("/profile")
    public Result<User> profile(@RequestAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) return Result.fail("未登录");
        User u = userService.getById(userId);
        u.setPassword(null);
        return Result.ok(u);
    }

    @PostMapping("/modifyPassword")
    public Result<String> modifyPassword(@RequestAttribute(value = "userId") Long userId, @RequestAttribute(value = "oldPassword") String oldPassword, @RequestAttribute(value = "newPassword") String newPassword) {
        userService.resetPassword(userId,oldPassword,newPassword);
        if (newPassword == null){
            return Result.ok("密码重置成功!");
        }
        return Result.ok("密码修改成功!");
    }

}

