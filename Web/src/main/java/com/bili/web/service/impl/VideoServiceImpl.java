package com.bili.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bili.pojo.constant.RedisConstants;
import com.bili.common.utils.PageInfo;
import com.bili.common.utils.Result;
import com.bili.common.utils.UserDTOHolder;
import com.bili.pojo.entity.*;
import com.bili.pojo.dto.AddVideoDTO;
import com.bili.pojo.dto.EditVideoDTO;
import com.bili.pojo.dto.VideoPageQueryDTO;
import com.bili.pojo.vo.MyOperation;
import com.bili.pojo.vo.VideoPageVO;
import com.bili.pojo.vo.VideoVO;
import com.bili.web.mapper.*;
import com.bili.web.service.IUserInfoService;
import com.bili.web.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService{


    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VideoSectionMapper videoSectionMapper;
    @Autowired
    private VideoStatusMapper videoStatusMapper;
    @Autowired
    private VideoTypeMapper videoTypeMapper;
    @Autowired
    private VideoTagsMapper videoTagsMapper;
    @Autowired
    private VideoTagMapMapper videoTagMapMapper;
    @Autowired
    private VideoCollectionsMapper videoCollectionsMapper;
    @Autowired
    private IUserInfoService userInfoService;


    /**
     * 根据视频ID获取视频详情。
     * @param id 视频的唯一标识符。
     * @return 包含视频详细信息的VideoVO对象。
     */
    @Override
    public VideoVO getVideoById(Integer id) {
        // 根据ID查询视频信息
        Video video = getById(id);
        //非空校验
        if(video == null){
            return null;
        }
        // 查询视频关联信息
        String sectionName = videoSectionMapper.getSectionName(video.getSectionId());
        String statusName = videoStatusMapper.getStatusName(video.getStatusId());
        String typeName = videoTypeMapper.getTypeName(video.getType());
        List<String> tags = videoMapper.getVideoTags(id);
        // 创建一个VideoVO对象，并将视频信息复制到其中
        VideoVO videoVO = new VideoVO();
        BeanUtils.copyProperties(video, videoVO);
        videoVO.setSection(sectionName);
        videoVO.setStatus(statusName);
        videoVO.setType(typeName);
        videoVO.setTags(tags);
        //返回结果
        return videoVO;
    }

    /**
     * 批量删除视频
     * @param ids
     */
    @Override
    public void deleteVideoByIds(List<Integer> ids) {
        //1.删除关联
        for (Integer id : ids) {
            videoTagMapMapper.delete(new LambdaQueryWrapper<VideoTagMap>().eq(VideoTagMap::getVideoId, id));
        }
        //2.删除视频
        videoMapper.deleteBatchIds(ids);
    }

    /**
     * 分页查询视频
     * @param query
     * @return
     */
    @Override
    public PageInfo<VideoPageVO> pageVideo(VideoPageQueryDTO query) {
        //1.构建条件
        //1.1.分页条件
        Page<Video> page = Page.of(query.getPage(), query.getPageSize());
        //1.2.排序条件
        //排序方式（1最多播放 2最新发布 3最多弹幕 4最多收藏）
        if(query.getOrder() != null){
            switch (query.getOrder()){
                case 1:
                    //按照播放量排序
                    page.addOrder(OrderItem.desc("view_count"));
                    break;
                case 2:
                    //按照发布时间排序
                    page.addOrder(OrderItem.desc("create_time"));
                    break;
                default:
                    //按照id排序
                    page.addOrder(OrderItem.desc("id"));
            }
        }else {
            //默认按照id排序
            page.addOrder(OrderItem.desc("id"));
        }

        //1.3.查询条件
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<Video>()
                .select(Video::getId, Video::getCoverImage, Video::getTitle, Video::getDuration, Video::getViewCount, Video::getUserId, Video::getCreateTime);
        if (query.getTitle() != null && !query.getTitle().isEmpty()) {
            wrapper.like(Video::getTitle, query.getTitle());
        }
        if (query.getSectionId() != null) {
            wrapper.eq(Video::getSectionId, query.getSectionId());
        }
        if (query.getMinDuration() != null && query.getMaxDuration() != null) {
            wrapper.ge(Video::getDuration, query.getMinDuration()).le(Video::getDuration, query.getMaxDuration());
        }

        //2.查询
        videoMapper.selectPage(page, wrapper);
        //3.数据非空校验
        List<Video> records = page.getRecords();
        if(records == null || records.size() == 0){
            //无数据返回空结果
            return new PageInfo<>(Collections.emptyList(),page.getTotal());
        }
        //4.有数据，转换
        List<VideoPageVO> list = new ArrayList<>();
        for (Video video : records) {
            Integer userId = video.getUserId();
            User user = userMapper.selectById(userId);
            //转换
            VideoPageVO videoPageVO = new VideoPageVO();
            BeanUtils.copyProperties(video, videoPageVO);
            videoPageVO.setUserNickName(user.getNickName());
            list.add(videoPageVO);
        }
        return new PageInfo(list, page.getTotal());
    }

    /**
     * 添加视频
     * @param addVideoDTO
     */
    @Override
    public void addVideo(AddVideoDTO addVideoDTO) {
        //获取用户id
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //Long userId = loginUser.getUser().getId();
        Long userId = 1L;
        //1.存入基本信息
        Video video = new Video();
        BeanUtil.copyProperties(addVideoDTO, video);
        video.setUserId(userId.intValue());
        video.setDuration(addVideoDTO.getDuration());
        video.setStatusId(0);
        video.setViewCount(0);
        video.setCreateTime(LocalDateTime.now());
        video.setUpdateTime(LocalDateTime.now());
        save(video);

        //2.存入标签id
        for (String tag : addVideoDTO.getTags()) {
            //2.1 插入标签
            Integer videoTagId = setVideoTag(tag);
            //2.2 设置标签视频关联表
            VideoTagMap videoTagMap = new VideoTagMap();
            videoTagMap.setVideoId(video.getId());
            videoTagMap.setTagId(videoTagId);
            videoTagMapMapper.insert(videoTagMap);
        }
    }

    /**
     * 添加标签
     * @param tag
     * @return
     */
    private Integer setVideoTag(String tag){
        //检查标签是否已经存在
        LambdaQueryWrapper<VideoTags> queryWrapper = new LambdaQueryWrapper<VideoTags>().like(VideoTags::getName, tag);
        List<Map<String, Object>> maps = videoTagsMapper.selectMaps(queryWrapper);
        if(maps.isEmpty()){
            //新建标签
            VideoTags videoTags = new VideoTags();
            videoTags.setName(tag);
            videoTags.setCreateTime(LocalDateTime.now());
            videoTagsMapper.insert(videoTags);
            return videoTags.getId();
        }
        return (Integer) maps.get(0).get("id");
    }

    /**
     * 修改视频信息
     * @param editVideoDTO
     */
    @Override
    public void editVideo(EditVideoDTO editVideoDTO) {
        //1.构建更新对象
        Video video = new Video();
        BeanUtil.copyProperties(editVideoDTO, video);
        videoMapper.updateById(video);
        //2.重新设置标签关联
        //2.1 删除标签视频关联
        videoTagMapMapper.delete(new LambdaQueryWrapper<VideoTagMap>().eq(VideoTagMap::getVideoId, video.getId()));
        //2.2 重新设置标签视频关联
        List<String> tags = editVideoDTO.getTags();
        for (String tag : tags) {
            //3.1 插入标签
            Integer videoTagId = setVideoTag(tag);
            //3.2 设置标签视频关联表
            VideoTagMap videoTagMap = new VideoTagMap();
            videoTagMap.setVideoId(video.getId());
            videoTagMap.setTagId(videoTagId);
            videoTagMapMapper.insert(videoTagMap);
        }
    }

    /**
     * 视频点赞与取消
     * @param videoId
     */
    @Override
    public Result likeVideo(Long videoId) {
        //1.获取用户信息
        Long userId = UserDTOHolder.getUserDTO().getId();
        //2.判断是否已经点赞
        String key = RedisConstants.VIDEO_LIKE_KEY + videoId;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        if(score == null){
            //2.2 还未点赞，添加点赞
            boolean isSuccess = update().setSql("likes = likes + 1").eq("id", videoId).update();
            if(isSuccess){
                //添加到redis
                stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
            }
        }else {
            //2.1 已经点赞，取消点赞
            boolean isSuccess = update().setSql("likes = likes - 1").eq("id", videoId).update();
            if(isSuccess){
                //添加到redis
                stringRedisTemplate.opsForZSet().remove(key, userId.toString());
            }
        }
        return Result.success();
    }

    /**
     * 视频投币
     * @param videoId
     * @return
     */
    @Override
    @Transactional
    public Result coinVideo(Long videoId, Integer count) {
        //1.获取用户信息
        Long userId = UserDTOHolder.getUserDTO().getId();
        // 判断是否是今天前五次投币，如果是，增加10exp
        isFirstFiveCoin(userId);
        //2.判断是否已经投币
        String key = RedisConstants.VIDEO_COIN_KEY + videoId;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        boolean userBool;
        if(score == null){
            //2.2 还未投币，添加投币
            //2.3 判断用户硬币是否足够
            Integer currentCoins = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", userId)).getCoins();
            // 检查是否有足够的 coins 进行扣减
            if (currentCoins < count) {
                // 返回错误信息
                return Result.failed("您的硬币不足！");
            } else {
                // 执行扣减操作
                userBool = userInfoService.update()
                        .eq("user_id", userId)
                        .setSql("coins = coins - " + count)
                        .update();
            }
            boolean videoBool = update().setSql("coins = coins + " + count).eq("id", videoId).update();
            if(userBool && videoBool){
                //添加到redis
                stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
            }
        }else {
            //2.1 已经投币，返回提示信息
            return Result.failed("请勿重复投币!");
        }
        return Result.success();
    }

    /**
     * 视频收藏与取消
     * @param videoId
     * @param collectionIds
     * @return
     */
    @Override
    public Result collectionVideo(Long videoId, List<Integer> collectionIds) {
        // 1. 获取用户信息
        Long userId = UserDTOHolder.getUserDTO().getId();
        // 2. 获取该用户所有收藏夹的id
        List<Integer> allCollectionIds = videoCollectionsMapper.getAllCollectionId(userId);

        // 3. 遍历获取所有收藏夹的id
        for (Integer collectionId : allCollectionIds) {
            String key = RedisConstants.USER_COLLECTION_KEY + userId + ":" + collectionId;
            // 4. 检查视频是否在collectionIds中
            if (collectionIds.contains(collectionId)) {
                //遍历collectionIds，如果视频在其中不做任何操作，不在其中新增
                Double score = stringRedisTemplate.opsForZSet().score(key, videoId.toString());
                if (score == null) {
                    stringRedisTemplate.opsForZSet().add(key, videoId.toString(), System.currentTimeMillis());
                }
            } else {
                //将collectionNameList以外的收藏删除
                stringRedisTemplate.opsForZSet().remove(key, videoId.toString());
            }
        }
        return Result.success();
    }

    /**
     * 判断是否是今天前五次投币，如果是，增加10exp
     * @param userId
     * @return
     */
    public Result isFirstFiveCoin(Long userId){
        // 2. 获取日期
        LocalDateTime now = LocalDateTime.now();
        // 3. 拼接key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = RedisConstants.USER_COIN_KEY + userId + keySuffix;
        // 4. 获取今天是本月的第几天
        int dayOfMonth = now.getDayOfMonth();
        // 5. 检查今天已经投币的次数
        String coinCountStr = stringRedisTemplate.opsForValue().get(key);
        Long coinCount;
        if (coinCountStr == null) {
            coinCount = 0L;
        }else {
            coinCount = Long.valueOf(coinCountStr);
        }
        // 6. 如果今天已经投币超过5次，则不增加经验值
        if (coinCount >= 5) {
            return Result.failed("今天已经投币超过5次");
        }
        // 7. 还未达到五次投币，写入Redis并增加计数器
        stringRedisTemplate.opsForValue().set(key, String.valueOf(coinCount + 1));
        // 8. 增加10exp
        userInfoService.update()
                .eq("user_id", userId)
                .setSql("experience = experience + 10")
                .update();
        return Result.success();
    }


    /**
     * 视频分享
     * @param videoId
     * @return
     */
    @Override
    public Result shareVideo(Long videoId) {
        //1. 获取用户信息
        Long userId = UserDTOHolder.getUserDTO().getId();
        //判断是否是今天第一次分享，如果是，增加5exp
        isFirstShare(userId);
        //2.判断是否已经分享过
        String key = RedisConstants.VIDEO_SHARE_KEY + videoId;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        if(score == null){
            //2.2 还未分享，添加分享数
            boolean isSuccess = update().setSql("shares = shares + 1").eq("id", videoId).update();
            if(isSuccess){
                //添加到redis
                stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
            }
        }
        //已经分享过，不增加次数
        Video video = getById(videoId);
        //3.构造数据返回
        if(video == null){
            return Result.failed("视频不存在！");
        }
        String share = "【" + video.getTitle() + "】" + " " + video.getVideoPath();
        return Result.success(share);
    }

    /**
     * 判断是否是今天第一次分享，如果是，增加5exp
     * @param userId
     * @return
     */
    public Result isFirstShare(Long userId){
        // 2.获取日期
        LocalDateTime now = LocalDateTime.now();
        // 3.拼接key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = RedisConstants.USER_SHARE_KEY + userId + keySuffix;
        // 4.获取今天是本月的第几天
        int dayOfMonth = now.getDayOfMonth();
        // 检查是否已经进行第一次分享
        boolean isSigned = stringRedisTemplate.opsForValue().getBit(key, dayOfMonth - 1);
        if (isSigned) {
            return Result.failed("不是第一次分享");
        }
        // 5.还未进行第一次分享，写入Redis
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
        //6.增加5exp
        userInfoService.update()
                .eq("user_id", userId)
                .setSql("experience = experience + 5")
                .update();
        return Result.success();
    }

    /**
     * 批量查询视频
     * @param ids
     * @return
     */
    @Override
    public Result getVideoByIds(List<Integer> ids) {
        ArrayList<VideoVO> videoVOS = new ArrayList<>();
        for (Integer id : ids) {
            VideoVO videoVO = getVideoById(id);
            videoVOS.add(videoVO);
        }
        return Result.success(videoVOS);
    }


    /**
     * 查询是否点赞投币收藏
     * @param videoId
     * @return
     */
    @Override
    public Result getMyOperation(Long videoId) {
        //1.获取用户信息
        Long userId = UserDTOHolder.getUserDTO().getId();
        //2.判断是否已经点赞
        String likeKey = RedisConstants.VIDEO_LIKE_KEY + videoId;
        Double likeScore = stringRedisTemplate.opsForZSet().score(likeKey, userId.toString());
        //3.判断是否已经投币
        String coinKey = RedisConstants.VIDEO_COIN_KEY + videoId;
        Double coinScore = stringRedisTemplate.opsForZSet().score(coinKey, userId.toString());
        //3.是否收藏
        //3.1. 获取该用户所有收藏夹的id
        List<Integer> allCollectionIds = videoCollectionsMapper.getAllCollectionId(userId);
        //3.2 遍历获取所有收藏夹的id
        Double collectScore = null;
        for (Integer collectionId : allCollectionIds) {
            String collectKey = RedisConstants.USER_COLLECTION_KEY + userId + ":" + collectionId;
            collectScore = stringRedisTemplate.opsForZSet().score(collectKey, videoId.toString());
            if(collectScore != null){
                 break;
            }
        }
        //3.3 返回数据
        MyOperation operation = new MyOperation(likeScore != null, coinScore != null, collectScore != null);
        return Result.success(operation);
    }

    /**
     * 查询是否是今天第一次观看视频
     * @return
     */
    @Override
    public Result isFirstVideoView() {
        //1.获取用户信息
        Long userId = UserDTOHolder.getUserDTO().getId();
        // 2.获取日期
        LocalDateTime now = LocalDateTime.now();
        // 3.拼接key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = RedisConstants.USER_VIDEO_KEY + userId + keySuffix;
        // 4.获取今天是本月的第几天
        int dayOfMonth = now.getDayOfMonth();
        // 检查是否已经进行第一次看视频
        boolean isSigned = stringRedisTemplate.opsForValue().getBit(key, dayOfMonth - 1);
        if (isSigned) {
            return Result.failed("不是第一次看视频");
        }
        // 5.还未进行第一次分享，写入Redis
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
        //6.增加5exp
        userInfoService.update()
                .eq("user_id", userId)
                .setSql("experience = experience + 5")
                .update();
        return Result.success();
    }
}

