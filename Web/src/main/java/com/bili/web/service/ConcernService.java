package com.bili.web.service;

import com.bili.common.dto.PageSelectWithIdParam;
import com.bili.common.utils.Result;
import org.springframework.stereotype.Service;

@Service
public interface ConcernService {
    Result concernUser(Long userId, Long concernUserId);

    Result cancelConcern(Long userId, Long concernUserId);

    Result getConcernList(PageSelectWithIdParam pageSelectWithIdParam);

    Result getFansList(PageSelectWithIdParam pageSelectWithIdParam);
}
