package com.bili.pojo.mapper.admin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bili.pojo.entity.admin.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}