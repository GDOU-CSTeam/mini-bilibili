package com.bili.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bili.pojo.entity.VideoStatus;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 视频状态表 Mapper 接口
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-07-31
 */
public interface VideoStatusMapper extends BaseMapper<VideoStatus> {

    @Select("select name from video_status where id = #{statusId}")
    String getStatusName(Integer statusId);
}
