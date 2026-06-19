package com.minimall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minimall.entity.Product;

public interface ProductService {
    Page<Product> page(Integer page, Integer size, String search, String categorySlug);
    Product getById(Integer id);
    Product getBySlug(String slug);
    Product create(Product product);
    Product update(Integer id, Product product);
    void delete(Integer id);
}
