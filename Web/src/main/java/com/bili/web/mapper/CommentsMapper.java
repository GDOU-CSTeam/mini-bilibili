package com.bili.web.mapper;

import com.bili.pojo.entity.Comments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 视频评论表 Mapper 接口
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:14
 */
@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {

}
