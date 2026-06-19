package com.minimall.controller.admin;

import com.minimall.common.Result;
import com.minimall.entity.Category;
import com.minimall.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Result<List<Category>> list() {
        return Result.ok(categoryService.listAll());
    }

    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Integer id) {
        return Result.ok(categoryService.getById(id));
    }

    @PostMapping
    public Result<Category> create(@RequestBody Category category) {
        return Result.ok("创建成功", categoryService.create(category));
    }

    @PutMapping("/{id}")
    public Result<Category> update(@PathVariable Integer id, @RequestBody Category category) {
        return Result.ok("更新成功", categoryService.update(id, category));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return Result.ok("删除成功", null);
    }
}
