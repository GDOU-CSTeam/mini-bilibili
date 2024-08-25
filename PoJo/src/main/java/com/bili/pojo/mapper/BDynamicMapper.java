package com.bili.pojo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bili.pojo.entity.BDynamic;
import com.bili.pojo.vo.DynamicShowVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lin
 * @since 2024-08-07 08:44:50
 */
@Mapper
public interface BDynamicMapper extends BaseMapper<BDynamic> {

    //查询关注用户的动态
    Page<DynamicShowVo> selectConcernUserDynamic(Long userId, @Param("page") Page<DynamicShowVo> page);

    //查询用户的动态
    Page<DynamicShowVo> selectUserDynamic(Long userId, @Param("page") Page<DynamicShowVo> page);
}
