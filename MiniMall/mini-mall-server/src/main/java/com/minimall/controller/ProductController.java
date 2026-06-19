package com.minimall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minimall.common.Result;
import com.minimall.entity.Product;
import com.minimall.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category) {
        Page<Product> result = productService.page(page, size, search, category);
        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("page", result.getCurrent());
        data.put("totalPages", result.getPages());
        return Result.ok(data);
    }

    @GetMapping("/{id}")
    public Result<Product> detail(@PathVariable Integer id) {
        return Result.ok(productService.getById(id));
    }
}
