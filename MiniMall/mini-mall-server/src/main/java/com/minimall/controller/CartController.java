package com.minimall.controller;

import com.minimall.common.Result;
import com.minimall.dto.CartAddRequest;
import com.minimall.entity.CartItem;
import com.minimall.security.SecurityUtils;
import com.minimall.service.CartService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Result<List<CartItem>> list() {
        Integer userId = SecurityUtils.getCurrentUserId();
        return Result.ok(cartService.listByUser(userId));
    }

    @PostMapping
    public Result<CartItem> add(@Valid @RequestBody CartAddRequest request) {
        Integer userId = SecurityUtils.getCurrentUserId();
        return Result.ok(cartService.add(userId, request.getProductId(), request.getQuantity()));
    }

    @PutMapping("/{cartItemId}")
    public Result<CartItem> updateQuantity(
            @PathVariable Integer cartItemId,
            @RequestBody CartAddRequest request) {
        Integer userId = SecurityUtils.getCurrentUserId();
        return Result.ok(cartService.updateQuantity(userId, cartItemId, request.getQuantity()));
    }

    @DeleteMapping("/{cartItemId}")
    public Result<?> remove(@PathVariable Integer cartItemId) {
        Integer userId = SecurityUtils.getCurrentUserId();
        cartService.remove(userId, cartItemId);
        return Result.ok("已移除", null);
    }

    @DeleteMapping
    public Result<?> clear() {
        Integer userId = SecurityUtils.getCurrentUserId();
        cartService.clear(userId);
        return Result.ok("购物车已清空", null);
    }
}
