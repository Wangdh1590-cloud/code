package com.minimall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minimall.common.Result;
import com.minimall.entity.Order;
import com.minimall.security.SecurityUtils;
import com.minimall.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Integer userId = SecurityUtils.getCurrentUserId();
        Page<Order> result = orderService.pageByUser(userId, page, size);
        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("page", result.getCurrent());
        data.put("totalPages", result.getPages());
        return Result.ok(data);
    }

    @PostMapping
    public Result<Order> create() {
        Integer userId = SecurityUtils.getCurrentUserId();
        return Result.ok("下单成功", orderService.create(userId));
    }

    @GetMapping("/{id}")
    public Result<Order> detail(@PathVariable Integer id) {
        Integer userId = SecurityUtils.getCurrentUserId();
        return Result.ok(orderService.getByIdWithCheck(id, userId));
    }

    @PostMapping("/{id}/pay")
    public Result<Order> pay(@PathVariable Integer id) {
        Integer userId = SecurityUtils.getCurrentUserId();
        return Result.ok("支付成功", orderService.pay(userId, id));
    }
}
