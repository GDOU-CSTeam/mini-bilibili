package com.bili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bili.dto.AddVideoDTO;
import com.bili.dto.EditVideoDTO;
import com.bili.dto.VideoPageQueryDTO;
import com.bili.entity.Video;
import com.bili.utils.PageInfo;
import com.bili.utils.Result;
import com.bili.vo.VideoPageVO;
import com.bili.vo.VideoVO;

import java.util.List;

public interface VideoService extends IService<Video> {

    /**
     * 根据id查询视频
     * @param id
     */
    VideoVO getVideoById(Integer id);

    /**
     * 批量删除视频
     * @param ids
     */
    void deleteVideoByIds(List<Integer> ids);

    /**
     * 分页查询视频
     * @param pageQueryDTO
     * @return
     */
    PageInfo<VideoPageVO> pageVideo(VideoPageQueryDTO pageQueryDTO);

    /**
     * 添加视频
     * @param addVideoDTO
     */
    void addVideo(AddVideoDTO addVideoDTO);

    /**
     * 修改视频信息
     * @param editVideoDTO
     */
    void editVideo(EditVideoDTO editVideoDTO);

    /**
     * 视频点赞与取消
     * @param videoId
     */
    Result likeVideo(Long videoId);

    /**
     * coinVideo
     * @param videoId
     * @return
     */
    Result coinVideo(Long videoId, Integer count);

    /**
     * 视频收藏与取消
     * @param videoId
     * @param collectionIds
     * @return
     */
    Result collectionVideo(Long videoId, List<Integer> collectionIds);

    /**
     * 视频分享
     * @param videoId
     * @return
     */
    Result shareVideo(Long videoId);

    /**
     * 批量查询视频
     * @param ids
     * @return
     */
    Result getVideoByIds(List<Integer> ids);

    /**
     * 查询是否点赞投币收藏
     * @param videoId
     * @return
     */
    Result getMyOperation(Long videoId);

    /**
     * 查询是否是今天第一次观看视频
     * @return
     */
    Result isFirstVideoView();
}
