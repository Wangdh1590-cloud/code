package com.minimall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.minimall.common.BusinessException;
import com.minimall.entity.CartItem;
import com.minimall.entity.Product;
import com.minimall.mapper.CartItemMapper;
import com.minimall.mapper.ProductMapper;
import com.minimall.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;

    public CartServiceImpl(CartItemMapper cartItemMapper, ProductMapper productMapper) {
        this.cartItemMapper = cartItemMapper;
        this.productMapper = productMapper;
    }

    @Override
    public List<CartItem> listByUser(Integer userId) {
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId).orderByDesc(CartItem::getCreatedAt);
        List<CartItem> items = cartItemMapper.selectList(wrapper);
        // 填充分类信息到商品
        for (CartItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            item.setProduct(product);
        }
        return items;
    }

    @Override
    public CartItem add(Integer userId, Integer productId, Integer quantity) {
        // 验证商品存在
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }
        if (product.getIsActive() == 0) {
            throw new BusinessException(400, "商品已下架");
        }

        // 检查是否已在购物车中
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId).eq(CartItem::getProductId, productId);
        CartItem existing = cartItemMapper.selectOne(wrapper);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartItemMapper.updateById(existing);
            return existing;
        }

        CartItem item = new CartItem();
        item.setUserId(userId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        cartItemMapper.insert(item);
        return item;
    }

    @Override
    public CartItem updateQuantity(Integer userId, Integer cartItemId, Integer quantity) {
        CartItem item = getOwnedItem(userId, cartItemId);
        item.setQuantity(quantity);
        cartItemMapper.updateById(item);
        return item;
    }

    @Override
    public void remove(Integer userId, Integer cartItemId) {
        getOwnedItem(userId, cartItemId);
        cartItemMapper.deleteById(cartItemId);
    }

    @Override
    @Transactional
    public void clear(Integer userId) {
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUserId, userId);
        cartItemMapper.delete(wrapper);
    }

    private CartItem getOwnedItem(Integer userId, Integer cartItemId) {
        CartItem item = cartItemMapper.selectById(cartItemId);
        if (item == null) {
            throw new BusinessException(404, "购物车项不存在");
        }
        if (!item.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作");
        }
        return item;
    }
}
