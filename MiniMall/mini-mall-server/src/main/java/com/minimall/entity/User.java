package com.minimall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String email;
    private String password;
    private String name;
    private String role;       // CUSTOMER / ADMIN

    private String createdAt;
    private String updatedAt;
}
