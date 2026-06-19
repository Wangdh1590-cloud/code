package com.minimall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minimall.entity.Order;

public interface OrderService {
    Page<Order> pageByUser(Integer userId, Integer page, Integer size);
    Page<Order> pageAll(Integer page, Integer size, String status);
    Order getById(Integer id);
    Order getByIdWithCheck(Integer id, Integer userId);
    Order create(Integer userId);
    Order pay(Integer userId, Integer orderId);
    Order updateStatus(Integer orderId, String status);
}
