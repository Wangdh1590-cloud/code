package com.minimall.controller;

import com.minimall.common.Result;
import com.minimall.dto.LoginRequest;
import com.minimall.dto.RegisterRequest;
import com.minimall.dto.UserVO;
import com.minimall.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterRequest request) {
        UserVO userVO = userService.register(request);
        return Result.ok("注册成功", userVO);
    }

    @PostMapping("/login")
    public Result<UserVO> login(@Valid @RequestBody LoginRequest request) {
        UserVO userVO = userService.login(request);
        return Result.ok("登录成功", userVO);
    }
}
