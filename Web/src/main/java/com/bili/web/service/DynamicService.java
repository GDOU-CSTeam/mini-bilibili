package com.bili.web.service;

import com.bili.common.utils.Result;
import com.bili.pojo.dto.user.PublishDynamicParam;
import com.bili.web.mq.bo.PublishDynamicToMq;
import org.springframework.stereotype.Service;

@Service
public interface DynamicService {

    void publish(PublishDynamicToMq publishDynamicToMq);

    Result publishToMq(Long userId, PublishDynamicParam publishDynamicParam);
}
