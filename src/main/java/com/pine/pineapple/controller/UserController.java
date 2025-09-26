package com.pine.pineapple.controller;


import com.pine.pineapple.common.utils.Result;
import com.pine.pineapple.entity.User;
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
    public Result<?> register(@RequestBody User user) {
        try {
            userService.register(user);
            return Result.ok("注册成功", null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            String token = userService.login(username, password);
            return Result.ok("登录成功", Map.of("token", token));
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }


    @GetMapping("/profile")
    public Result<?> profile(@RequestAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) return Result.fail("未登录");
        User u = userService.getById(userId);
        u.setPassword(null);
        return Result.ok(u);
    }
}

