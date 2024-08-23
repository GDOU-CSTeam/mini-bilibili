package com.bili.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bili.constant.RedisConstants;
import com.bili.entity.VideoComments;
import com.bili.mapper.VideoCommentsMapper;
import com.bili.service.IVideoCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bili.service.UserService;
import com.bili.utils.PageInfo;
import com.bili.utils.Result;
import com.bili.utils.UserDTOHolder;
import com.bili.vo.VideoCommentVO;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 视频评论表 服务实现类
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-16
 */
@Service
public class VideoCommentsServiceImpl extends ServiceImpl<VideoCommentsMapper, VideoComments> implements IVideoCommentsService {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询视频评论
     * @param videoId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result pageComment(Integer videoId, Integer pageNum, Integer pageSize, Integer order) {
        //1.构建查询条件
        //1.1 分页参数
        Page<VideoComments> page = new Page(pageNum, pageSize);
        //1.2 排序条件
        if(order != null){
            switch (order){
                case 1:
                    //按最热排序
                    page.addOrder(OrderItem.desc("likes"));
                    break;
                case 2:
                    //按最新排序
                    page.addOrder(OrderItem.desc("create_time"));
                    break;
                default:
                    //按最热排序
                    page.addOrder(OrderItem.desc("likes"));
                    break;
            }
        }else{
            //按最热排序
            page.addOrder(OrderItem.desc("likes"));
        }
        //1.3 查询条件: 获取该视频的顶级评论
        LambdaQueryWrapper<VideoComments> queryWrapper = new LambdaQueryWrapper<VideoComments>()
                .eq(VideoComments::getVideoId, videoId)
                .eq(VideoComments::getParentId, 0)
                .eq(VideoComments::getDelFlag, 0);

        //2.查询数据库
        page(page, queryWrapper);

        //3.数据非空校验
        List<VideoComments> records = page.getRecords();
        if(records == null || records.size() == 0){
            //3.1 无数据，返回null
            return Result.success(null);
        }
        //3.2 有数据，进行数据转换
        List<VideoCommentVO> commentVOList = toVideoCommentVO(records);
        //3.3 查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (VideoCommentVO commentVO : commentVOList) {
            List<VideoCommentVO> childrenComment = getChildrenComment(commentVO.getId());
            commentVO.setChildren(childrenComment);
        }

        //4.查询评论总数
        long count = count(new LambdaQueryWrapper<VideoComments>().eq(VideoComments::getVideoId, videoId).eq(VideoComments::getDelFlag, 0));
        return Result.success(new PageInfo<VideoCommentVO>(commentVOList, count));
    }

    /**
     * 根据commentId获取子评论
     * @param commentId
     * @return
     */
    private List<VideoCommentVO> getChildrenComment(Integer commentId){
        //1.构建查询条件
        LambdaQueryWrapper<VideoComments> queryWrapper = new LambdaQueryWrapper<VideoComments>()
                .eq(VideoComments::getParentId, commentId)
                .eq(VideoComments::getDelFlag, 0)
                .orderByDesc(VideoComments::getCreateTime);
        //2.查询
        List<VideoComments> list = list(queryWrapper);
        //3.非空校验
        if(list == null || list.size() == 0){
            return Collections.emptyList();
        }
        //4.类型转换
        List<VideoCommentVO> videoCommentVOS = toVideoCommentVO(list);
        return videoCommentVOS;
    }

    /**
     * 父评论和子评论的数据转换
     * @param list
     * @return
     */
    private List<VideoCommentVO> toVideoCommentVO(List<VideoComments> list){
        List<VideoCommentVO> commentVOS = BeanUtil.copyToList(list, VideoCommentVO.class);
        for (VideoCommentVO commentVO : commentVOS) {
            //子评论额外的处理
            if(commentVO.getParentId() != 0){
                String replyUserName = userService.getById(commentVO.getReplyUserId()).getNickName();
                commentVO.setReplyUserName(replyUserName);
            }
        }
        return commentVOS;
    }


    /**
     * 添加评论
     * @param comments
     * @return
     */
    @Override
    public Result addComment(VideoComments comments) {
        Long userId = UserDTOHolder.getUserDTO().getId();
        comments.setUserId(userId.intValue());
        boolean isSuccess = save(comments);
        if(isSuccess){
            return Result.success();
        }
        return Result.failed();
    }

    /**
     * 删除评论
     * @param commentIds
     * @return
     */
    @Override
    public Result deleteComment(List<Integer> commentIds) {
        List<VideoComments> videoComments = listByIds(commentIds);
        for (VideoComments videoComment : videoComments) {
            //删除该评论
            update().setSql("del_flag = 1").eq("id", videoComment.getId()).update();
            if(videoComment.getParentId() == 0){
                //如果是父评论，删除旗下的子评论
                update().setSql("del_flag = 1").eq("parent_id", videoComment.getId()).update();
            }
        }
        return Result.success();
    }


    /**
     * 评论的点赞
     * @param commentId
     * @return
     */
    @Override
    public Result likeComment(Integer commentId) {
        //1.获取用户信息
        Long userId = UserDTOHolder.getUserDTO().getId();
        //2.判断是否已经点赞
        String key = RedisConstants.COLLECTION_LIKE_KEY + commentId;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        if(score == null){
            //2.2 还未点赞，添加点赞
            boolean isSuccess = update().setSql("likes = likes + 1").eq("id", commentId).update();
            if(isSuccess){
                //添加到redis
                stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
            }
        }else {
            //2.1 已经点赞，取消点赞
            boolean isSuccess = update().setSql("likes = likes - 1").eq("id", commentId).update();
            if(isSuccess){
                //添加到redis
                stringRedisTemplate.opsForZSet().remove(key, userId.toString());
            }
        }
        return Result.success();
    }
}


