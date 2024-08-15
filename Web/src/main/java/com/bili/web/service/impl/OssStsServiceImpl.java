package com.bili.web.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.bili.common.utils.AliyunOss;
import com.bili.common.utils.RedisCache;
import com.bili.common.utils.Result;
import com.bili.pojo.constant.user.WebRedisConstants;
import com.bili.pojo.dto.user.GetOssStsParam;
import com.bili.web.service.OssStsService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OssStsServiceImpl implements OssStsService {

    @Resource
    AliyunOss aliyunOss;
    @Resource
    RedisCache redisCache;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Result getOssStsImage(Long userId, GetOssStsParam getOssStsParam) throws ClientException {
        //判断字段是否为type: avatar-头像 background-背景图 dynamicImage-动态图片 chatImage-聊天图片 commentImage-评论图片其中之一
        List<String> validTypes = Arrays.asList("avatar", "background", "dynamicImage", "chatImage", "commentImage");
        if (validTypes.contains(getOssStsParam.getType())) {
            String fileName = aliyunOss.getFileName(getOssStsParam.getSuffix(), getOssStsParam.getType());
            long score = System.currentTimeMillis() + 1000 * 60 * 60;
            stringRedisTemplate.opsForZSet().add(WebRedisConstants.FILE_UPLOAD_RECORD_KEY, fileName, score);
            return Result.success(aliyunOss.getKey(fileName));
        }
        return Result.failed("type参数错误");
    }

    @Override
    public Result getOssStsVideo(Long userId, String suffix) throws ClientException {
        String fileName = aliyunOss.getFileName(suffix, "video");
        long score = System.currentTimeMillis() + 1000 * 60 * 60;
        stringRedisTemplate.opsForZSet().add(WebRedisConstants.FILE_UPLOAD_RECORD_KEY, fileName, score);
        return Result.success(aliyunOss.getKey(fileName));
    }
}
