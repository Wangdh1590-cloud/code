package com.minimall.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minimall.common.Result;
import com.minimall.entity.Order;
import com.minimall.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status) {
        Page<Order> result = orderService.pageAll(page, size, status);
        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("page", result.getCurrent());
        data.put("totalPages", result.getPages());
        return Result.ok(data);
    }

    @PutMapping("/{id}/status")
    public Result<Order> updateStatus(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return Result.ok(orderService.updateStatus(id, body.get("status")));
    }
}
