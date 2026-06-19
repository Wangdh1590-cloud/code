package com.minimall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minimall.common.BusinessException;
import com.minimall.entity.*;
import com.minimall.mapper.*;
import com.minimall.service.CartService;
import com.minimall.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartService cartService;
    private final ProductMapper productMapper;

    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper,
                            CartService cartService, ProductMapper productMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartService = cartService;
        this.productMapper = productMapper;
    }

    @Override
    public Page<Order> pageByUser(Integer userId, Integer page, Integer size) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId).orderByDesc(Order::getCreatedAt);
        Page<Order> resultPage = orderMapper.selectPage(new Page<>(page, size), wrapper);
        fillItems(resultPage.getRecords());
        return resultPage;
    }

    @Override
    public Page<Order> pageAll(Integer page, Integer size, String status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(status)) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreatedAt);
        Page<Order> resultPage = orderMapper.selectPage(new Page<>(page, size), wrapper);
        fillItems(resultPage.getRecords());
        return resultPage;
    }

    @Override
    public Order getById(Integer id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        fillItems(order);
        return order;
    }

    @Override
    public Order getByIdWithCheck(Integer id, Integer userId) {
        Order order = getById(id);
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权查看此订单");
        }
        return order;
    }

    @Override
    @Transactional
    public Order create(Integer userId) {
        // 获取购物车
        List<CartItem> cartItems = cartService.listByUser(userId);
        if (cartItems.isEmpty()) {
            throw new BusinessException(400, "购物车为空");
        }

        // 计算总金额并创建订单明细
        int totalAmount = 0;
        for (CartItem ci : cartItems) {
            Product p = ci.getProduct();
            if (p == null || p.getIsActive() == 0) {
                throw new BusinessException(400, "商品「" + (p != null ? p.getName() : "未知") + "」已下架，请先移除");
            }
            totalAmount += p.getPrice() * ci.getQuantity();
        }

        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PENDING");
        order.setTotalAmount(totalAmount);
        orderMapper.insert(order);

        // 创建订单明细
        for (CartItem ci : cartItems) {
            Product p = ci.getProduct();
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(p.getId());
            item.setProductName(p.getName());
            item.setProductPrice(p.getPrice());
            item.setQuantity(ci.getQuantity());
            orderItemMapper.insert(item);
        }

        // 清空购物车
        cartService.clear(userId);

        return getById(order.getId());
    }

    @Override
    @Transactional
    public Order pay(Integer userId, Integer orderId) {
        Order order = getByIdWithCheck(orderId, userId);
        if (!"PENDING".equals(order.getStatus())) {
            throw new BusinessException(400, "订单状态不允许支付");
        }
        order.setStatus("PAID");
        orderMapper.updateById(order);
        return getById(orderId);
    }

    @Override
    public Order updateStatus(Integer orderId, String status) {
        Order order = getById(orderId);
        order.setStatus(status);
        orderMapper.updateById(order);
        return getById(orderId);
    }

    private void fillItems(List<Order> orders) {
        for (Order order : orders) {
            fillItems(order);
        }
    }

    private void fillItems(Order order) {
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> items = orderItemMapper.selectList(wrapper);
        order.setItems(items);
    }
}
