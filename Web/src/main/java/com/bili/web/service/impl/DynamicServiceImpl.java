package com.bili.web.service.impl;

import com.bili.common.utils.AliyunOss;
import com.bili.common.utils.Result;
import com.bili.pojo.dto.user.PublishDynamicParam;
import com.bili.pojo.entity.user.BDynamic;
import com.bili.pojo.mapper.user.BDynamicMapper;
import com.bili.pojo.mapper.user.BDynamicReactionsMapper;
import com.bili.web.mq.bo.PublishDynamicToMq;
import com.bili.web.mq.config.RabbitConfig;
import com.bili.web.service.DynamicService;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DynamicServiceImpl implements DynamicService {

    @Resource
    BDynamicMapper dynamicMapper;
    @Resource
    BDynamicReactionsMapper dynamicReactionsMapper;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    Gson gson;
    @Resource
    AliyunOss aliyunOss;

    @Override
    public Result publishToMq(Long userId, PublishDynamicParam publishDynamicParam) {
        //检查标题是否大于25字
        if (publishDynamicParam.getTitle().length() > 25) {
            return Result.failed("标题不能超过25字");
        }
        //检查内容是否大于1000字
        if (publishDynamicParam.getContent().length() > 1000) {
            return Result.failed("内容不能超过200字");
        }
        if (publishDynamicParam.getVideoId() != null && !publishDynamicParam.getImages().isEmpty()) {
               return Result.failed("单次发布不能同时包含图片和视频");
        }
        if (publishDynamicParam.getImages().isEmpty()) {
            publishDynamicParam.setImages(null);
        } //检测图片是否大于9张
        else if (publishDynamicParam.getImages().size() > 9) {
                return Result.failed("图片不能超过9张");
        }
        else {
            int i = 1;
            for (String image : publishDynamicParam.getImages()) {
                //检查图片是否存在
                if (!aliyunOss.findFile(image)) {
                    return Result.failed("第" + i + "张图片不存在");
                }
                i++;
            }
        }
        //检查视频是否存在
        if (publishDynamicParam.getVideoId() != null && !publishDynamicParam.getVideoId().isBlank()) {
            //检查视频是否为纯数字
            if (!publishDynamicParam.getVideoId().matches("[0-9]+")) {
                return Result.failed("视频ID格式错误");
            }
            //省略
        }
        PublishDynamicToMq publishDynamicToMq = new PublishDynamicToMq();
        publishDynamicToMq.setUserId(userId);
        publishDynamicToMq.setPublishDynamicParam(publishDynamicParam);
        rabbitTemplate.convertAndSend(RabbitConfig.DYNAMIC_PUBLISH_QUEUE, gson.toJson(publishDynamicToMq));
        return Result.success("发布成功");
    }

    @Override
    public void publish(PublishDynamicToMq publishDynamicToMq) {
        BDynamic dynamic = new BDynamic();
        dynamic.setUserId(publishDynamicToMq.getUserId())
                .setTitle(publishDynamicToMq.getPublishDynamicParam().getTitle())
                .setContent(publishDynamicToMq.getPublishDynamicParam().getContent())
                .setImages(publishDynamicToMq.getPublishDynamicParam().getImages() == null ?
                        null : gson.toJson(publishDynamicToMq.getPublishDynamicParam().getImages()))
                .setVideoId(publishDynamicToMq.getPublishDynamicParam().getVideoId().isBlank() ? null :
                        Long.valueOf(publishDynamicToMq.getPublishDynamicParam().getVideoId()))
                .setCreateTime(LocalDateTime.now());
        dynamicMapper.insert(dynamic);
    }
}
