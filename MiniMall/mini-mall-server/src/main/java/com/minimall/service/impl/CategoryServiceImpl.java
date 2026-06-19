package com.minimall.service.impl;

import com.minimall.common.BusinessException;
import com.minimall.entity.Category;
import com.minimall.mapper.CategoryMapper;
import com.minimall.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> listAll() {
        return categoryMapper.selectList(null);
    }

    @Override
    public Category getById(Integer id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }
        return category;
    }

    @Override
    public Category create(Category category) {
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public Category update(Integer id, Category category) {
        getById(id);
        category.setId(id);
        categoryMapper.updateById(category);
        return getById(id);
    }

    @Override
    public void delete(Integer id) {
        getById(id);
        categoryMapper.deleteById(id);
    }
}
