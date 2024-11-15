package com.bili.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bili.pojo.constant.RedisConstants;
import com.bili.common.utils.Result;
import com.bili.common.utils.UserDTOHolder;
import com.bili.pojo.entity.VideoCollections;
import com.bili.pojo.vo.CollectionVO;
import com.bili.web.mapper.VideoCollectionsMapper;
import com.bili.web.service.IVideoCollectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 视频收藏夹表 服务实现类
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-15
 */
@Service
public class VideoCollectionsServiceImpl extends ServiceImpl<VideoCollectionsMapper, VideoCollections> implements IVideoCollectionsService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private VideoCollectionsMapper collectionsMapper;

    /**
     * 添加收藏夹
     * @param collectionDTO
     * @return
     */
    @Override
    public Result addCollection(VideoCollections collectionDTO) {
        //获取登录用户
        Long userId = UserDTOHolder.getUserDTO().getId();
        //构建插入对象
        VideoCollections videoCollections = new VideoCollections();
        BeanUtil.copyProperties(collectionDTO, videoCollections);
        videoCollections.setUserId(userId);
        save(videoCollections);
        return Result.success();
    }

    /**
     * 根据id查询收藏夹
     * @param collectionId
     * @return
     */
    @Override
    public CollectionVO getCollectionById(Integer collectionId) {
        //获取收藏夹的基本信息
        VideoCollections videoCollections = getById(collectionId);
        if(videoCollections == null){
            return null;
        }
        //获取收藏夹中的所有视频ID
        String key = RedisConstants.USER_COLLECTION_KEY + videoCollections.getUserId() + ":" + collectionId;
        Set<String> range = stringRedisTemplate.opsForZSet().range(key, 0, -1);
        //构建对象返回前端
        CollectionVO collectionVO = new CollectionVO();
        collectionVO.setVideoCollections(videoCollections);
        collectionVO.setVideoList(range);
        return collectionVO;
    }


    /**
     * 获取用户所有收藏夹
     * @return
     */
    @Override
    public Result getCollectionList() {
        Long userId = UserDTOHolder.getUserDTO().getId();
        List<VideoCollections> collections = collectionsMapper.getCollectionList(userId);
        return Result.success(collections);
    }

    /**
     * 删除收藏夹
     * @param collectionId
     * @return
     */
    @Override
    public Result deleteCollection(Integer collectionId) {
        Long userId = UserDTOHolder.getUserDTO().getId();
        //mysql中删除
        Boolean isSuccess = collectionsMapper.deleteCollection(collectionId, userId);
        //redis中删除
        if(isSuccess){
            String key = RedisConstants.USER_COLLECTION_KEY +  userId + ":" + collectionId;
            stringRedisTemplate.delete(key);
        }
        return Result.success();
    }

    /**
     * 修改收藏夹
     * @param videoCollections
     * @return
     */
    @Override
    public Result updateCollection(VideoCollections videoCollections) {
        boolean isSuccess = updateById(videoCollections);
        if(!isSuccess){
            return Result.failed();
        }
        return Result.success();
    }
}
