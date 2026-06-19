package com.minimall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.List;

@Data
@TableName("t_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String status;          // PENDING / PAID / SHIPPED / CANCELLED
    private Integer totalAmount;    // 分为单位

    private String createdAt;
    private String updatedAt;

    @TableField(exist = false)
    private List<OrderItem> items;
}
