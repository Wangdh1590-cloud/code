package com.minimall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("cart_item")
public class CartItem {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;

    private String createdAt;
    private String updatedAt;

    @TableField(exist = false)
    private Product product;
}
