package com.bili.web.service.impl;

import com.bili.common.utils.RedisCache;
import com.bili.common.utils.SseEmitterUtil;
import com.bili.pojo.constant.WebRedisConstants;
import com.bili.pojo.enums.LikeType;
import com.bili.pojo.enums.NoticeType;
import com.bili.web.service.NoticeService;
import jakarta.annotation.Resource;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Resource
    RedisCache redisCache;
    @Resource
    Redisson redisson;

    private void sendNotice(Long userId, Long ResourceId,NoticeType noticetype) throws InterruptedException {
        //动态通知
        if (noticetype.getValue() == NoticeType.DYNAMIC.getValue()) {
            SseEmitterUtil.sendMessage(String.valueOf(userId), "DynamicNotice");
            redisCache.setCacheMapValue(WebRedisConstants.DYNAMIC_NOTICE_KEY + userId, String.valueOf(ResourceId),
                    1);
        }
        //评论通知
        else if (noticetype.getValue() == NoticeType.COMMENT.getValue()) {
            SseEmitterUtil.sendMessage(String.valueOf(userId), "CommentNotice");
            redisCache.setCacheMapValue(WebRedisConstants.COMMENT_NOTICE_KEY + userId, String.valueOf(ResourceId),
                    1);
        }
        //点赞通知
        else if (noticetype.getValue() == NoticeType.LIKE.getValue()) {
            SseEmitterUtil.sendMessage(String.valueOf(userId), "LikeNotice");
            redisCache.setCacheMapValue(WebRedisConstants.LIKE_NOTICE_KEY + userId, String.valueOf(ResourceId),
                    1);
        }
        //聊天通知
        else if (noticetype.getValue() == NoticeType.CHAT.getValue()) {
            SseEmitterUtil.sendMessage(String.valueOf(userId), "ChatNotice");
            RLock lock = redisson.getLock(WebRedisConstants.CHAT_NOTICE_LOCK_KEY + userId);
            if (lock.tryLock()) {
                int count = redisCache.getCacheMapValue(WebRedisConstants.CHAT_NOTICE_KEY + userId,
                        String.valueOf(ResourceId));
                count++;
                redisCache.setCacheMapValue(WebRedisConstants.CHAT_NOTICE_KEY + userId, String.valueOf(ResourceId),
                        count);
            }
            else {
                //锁被占用，重新发送通知
                //休息一段时间再重新发送
                Thread.sleep(100);
                sendNotice(userId, ResourceId, noticetype);
            }
            lock.unlock();
        }
    }

    @Override
    public void sendNoticeWhenSendDynamicAndVideo(Long userId) {

    }

    @Override
    public void sendNoticeWhenComment(Long userId, Long commentId, Long replyId) {

    }

    @Override
    public void sendNoticeWhenLike(Long userId, Long Id, LikeType likeType) {

    }

    @Override
    public void sendNoticeWhenChat(Long userId) {

    }
}
