package com.minimall.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minimall.common.Result;
import com.minimall.entity.Product;
import com.minimall.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categorySlug) {
        Page<Product> result = productService.pageAll(page, size, search, categorySlug);
        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("page", result.getCurrent());
        data.put("totalPages", result.getPages());
        return Result.ok(data);
    }

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Integer id) {
        return Result.ok(productService.getById(id));
    }

    @PostMapping
    public Result<Product> create(@RequestBody Product product) {
        return Result.ok("创建成功", productService.create(product));
    }

    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Integer id, @RequestBody Product product) {
        return Result.ok("更新成功", productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        productService.delete(id);
        return Result.ok("删除成功", null);
    }
}
