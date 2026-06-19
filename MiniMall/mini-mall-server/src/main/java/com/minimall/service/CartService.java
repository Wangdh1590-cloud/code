package com.minimall.service;

import com.minimall.entity.CartItem;
import java.util.List;

public interface CartService {
    List<CartItem> listByUser(Integer userId);
    CartItem add(Integer userId, Integer productId, Integer quantity);
    CartItem updateQuantity(Integer userId, Integer cartItemId, Integer quantity);
    void remove(Integer userId, Integer cartItemId);
    void clear(Integer userId);
}
