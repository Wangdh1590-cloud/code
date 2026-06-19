package com.minimall.service;

import com.minimall.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> listAll();
    Category getById(Integer id);
    Category create(Category category);
    Category update(Integer id, Category category);
    void delete(Integer id);
}
