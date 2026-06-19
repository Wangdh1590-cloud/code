package com.minimall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minimall.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
}
