package com.bili.pojo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bili.pojo.entity.VideoCollections;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 视频收藏夹表 Mapper 接口
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-15
 */
public interface VideoCollectionsMapper extends BaseMapper<VideoCollections> {

    @Select("select id from video_collections where user_id = #{userId}")
    List<Integer> getAllCollectionId(Long userId);

    @Select("select * from video_collections where user_id = #{userId}")
    List<VideoCollections> getCollectionList(Long userId);

    @Delete("delete from video_collections where user_id = #{userId} and id = #{collectionId}")
    Boolean deleteCollection(Integer collectionId, Long userId);
}
