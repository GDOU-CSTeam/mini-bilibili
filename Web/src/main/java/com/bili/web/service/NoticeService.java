package com.bili.web.service;

import com.bili.pojo.enums.LikeType;
import com.bili.pojo.enums.NoticeType;
import org.springframework.stereotype.Service;

@Service
public interface NoticeService {

    void sendNoticeWhenSendDynamicAndVideo(Long userId);

    void sendNoticeWhenComment(Long userId, Long commentId, Long replyId);

    void sendNoticeWhenLike(Long userId, Long Id, LikeType likeType);

    void sendNoticeWhenChat(Long userId);
}
