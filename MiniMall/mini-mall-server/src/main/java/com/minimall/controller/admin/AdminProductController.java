package com.minimall.controller.admin;

import com.minimall.common.Result;
import com.minimall.entity.Product;
import com.minimall.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
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
