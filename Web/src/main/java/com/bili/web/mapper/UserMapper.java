package com.bili.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bili.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}