package com.bili.mapper;

import com.bili.entity.VideoType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 视频类型表 Mapper 接口
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-07-31
 */
public interface VideoTypeMapper extends BaseMapper<VideoType> {

    @Select("select name from video_type where id = #{typeId}")
    String getTypeName(Integer typeId);
}
