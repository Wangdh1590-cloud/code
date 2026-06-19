package com.minimall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("order_item")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private String productName;     // 快照
    private Integer productPrice;   // 快照（分）
    private Integer quantity;
}
