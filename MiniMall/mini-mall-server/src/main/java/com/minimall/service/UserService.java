package com.minimall.service;

import com.minimall.dto.LoginRequest;
import com.minimall.dto.RegisterRequest;
import com.minimall.dto.UserVO;

public interface UserService {
    UserVO register(RegisterRequest request);
    UserVO login(LoginRequest request);
}
