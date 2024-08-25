package com.bili.pojo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bili.pojo.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);
}
