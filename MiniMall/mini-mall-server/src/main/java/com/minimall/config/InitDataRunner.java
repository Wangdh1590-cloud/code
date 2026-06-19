package com.minimall.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.minimall.entity.User;
import com.minimall.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitDataRunner implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public InitDataRunner(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 创建管理员账号（如果不存在）
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, "admin@minimall.com");
        if (userMapper.selectCount(wrapper) == 0) {
            User admin = new User();
            admin.setEmail("admin@minimall.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("管理员");
            admin.setRole("ADMIN");
            userMapper.insert(admin);
            System.out.println("=== 管理员账号已创建: admin@minimall.com / admin123 ===");
        }

        // 创建测试用户（如果不存在）
        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, "user@minimall.com");
        if (userMapper.selectCount(wrapper) == 0) {
            User user = new User();
            user.setEmail("user@minimall.com");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setName("测试用户");
            user.setRole("CUSTOMER");
            userMapper.insert(user);
            System.out.println("=== 测试用户已创建: user@minimall.com / 123456 ===");
        }
    }
}
