package com.minimall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minimall.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
