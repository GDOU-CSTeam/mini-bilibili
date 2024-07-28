package com.bili.pojo.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bili.pojo.entity.user.BConcern;
import com.bili.pojo.vo.user.UserInfoShowVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户关注表 Mapper 接口
 * </p>
 *
 * @author lin
 * @since 2024-07-24 05:19:39
 */
@Mapper
public interface BConcernMapper extends BaseMapper<BConcern> {

    //查询关注的用户
    Page<UserInfoShowVo> selectConcernedByUserId(Long userId, @Param("page") Page<UserInfoShowVo> page);

    //查询粉丝
    Page<UserInfoShowVo> selectFansByUserId(Long userId, @Param("page") Page<UserInfoShowVo> page);
}
