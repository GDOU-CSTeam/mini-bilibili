package com.bili.web.service;

import com.bili.common.dto.PageSelectParam;
import com.bili.common.dto.PageSelectWithIdParam;
import com.bili.common.utils.Result;
import com.bili.pojo.dto.PublishDynamicParam;
import com.bili.web.mq.bo.PublishDynamicToMq;
import org.springframework.stereotype.Service;

@Service
public interface DynamicService {

    void publish(PublishDynamicToMq publishDynamicToMq);

    Result publishToMq(Long userId, PublishDynamicParam publishDynamicParam);

    Result getConcernUserDynamic(Long userId, PageSelectParam pageSelectParam);

    Result getUserDynamic(PageSelectWithIdParam pageSelectWithIdParam);

    Result delete(Long userId, Long dynamicId);
}
