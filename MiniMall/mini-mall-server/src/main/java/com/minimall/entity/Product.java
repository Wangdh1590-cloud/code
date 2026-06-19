package com.minimall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String slug;
    private String description;
    private Integer price;          // 分为单位
    private String imageUrl;
    private Integer stock;
    private Integer isActive;       // 1=上架, 0=下架
    private Integer categoryId;

    private String createdAt;
    private String updatedAt;

    @TableField(exist = false)
    private Category category;      // 关联查询用
}
