package com.minimall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minimall.common.BusinessException;
import com.minimall.entity.Category;
import com.minimall.entity.Product;
import com.minimall.mapper.CategoryMapper;
import com.minimall.mapper.ProductMapper;
import com.minimall.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    public ProductServiceImpl(ProductMapper productMapper, CategoryMapper categoryMapper) {
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Page<Product> page(Integer page, Integer size, String search, String categorySlug) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getIsActive, 1);

        // 分类筛选
        if (StringUtils.hasText(categorySlug)) {
            LambdaQueryWrapper<Category> catWrapper = new LambdaQueryWrapper<>();
            catWrapper.eq(Category::getSlug, categorySlug);
            Category category = categoryMapper.selectOne(catWrapper);
            if (category != null) {
                wrapper.eq(Product::getCategoryId, category.getId());
            }
        }

        // 搜索
        if (StringUtils.hasText(search)) {
            wrapper.and(w -> w.like(Product::getName, search).or().like(Product::getDescription, search));
        }

        wrapper.orderByDesc(Product::getCreatedAt);

        Page<Product> resultPage = productMapper.selectPage(new Page<>(page, size), wrapper);

        // 填充分类信息
        for (Product product : resultPage.getRecords()) {
            Category category = categoryMapper.selectById(product.getCategoryId());
            product.setCategory(category);
        }

        return resultPage;
    }

    @Override
    public Page<Product> pageAll(Integer page, Integer size, String search, String categorySlug) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        // 管理员可查看所有商品（含下架的），不限制 isActive

        if (StringUtils.hasText(categorySlug)) {
            LambdaQueryWrapper<Category> catWrapper = new LambdaQueryWrapper<>();
            catWrapper.eq(Category::getSlug, categorySlug);
            Category category = categoryMapper.selectOne(catWrapper);
            if (category != null) {
                wrapper.eq(Product::getCategoryId, category.getId());
            }
        }

        if (StringUtils.hasText(search)) {
            wrapper.and(w -> w.like(Product::getName, search).or().like(Product::getDescription, search));
        }

        wrapper.orderByDesc(Product::getCreatedAt);

        Page<Product> resultPage = productMapper.selectPage(new Page<>(page, size), wrapper);

        for (Product product : resultPage.getRecords()) {
            Category category = categoryMapper.selectById(product.getCategoryId());
            product.setCategory(category);
        }

        return resultPage;
    }

    @Override
    public Product getById(Integer id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        Category category = categoryMapper.selectById(product.getCategoryId());
        product.setCategory(category);
        return product;
    }

    @Override
    public Product getBySlug(String slug) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getSlug, slug).eq(Product::getIsActive, 1);
        Product product = productMapper.selectOne(wrapper);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        Category category = categoryMapper.selectById(product.getCategoryId());
        product.setCategory(category);
        return product;
    }

    @Override
    public Product create(Product product) {
        productMapper.insert(product);
        return getById(product.getId());
    }

    @Override
    public Product update(Integer id, Product product) {
        getById(id);
        product.setId(id);
        productMapper.updateById(product);
        return getById(id);
    }

    @Override
    public void delete(Integer id) {
        getById(id);
        productMapper.deleteById(id);
    }
}
