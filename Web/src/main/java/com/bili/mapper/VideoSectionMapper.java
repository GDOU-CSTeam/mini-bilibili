package com.bili.mapper;

import com.bili.entity.VideoSection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 视频分区表 Mapper 接口
 * </p>
 *
 * @author hygl
 * @since 2024-07-31
 */
public interface VideoSectionMapper extends BaseMapper<VideoSection> {

    @Select("select name from video_section where id = #{sectionId}")
    String getSectionName(Integer sectionId);
}
