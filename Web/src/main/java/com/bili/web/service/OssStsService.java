package com.bili.web.service;

import com.aliyuncs.exceptions.ClientException;
import com.bili.common.utils.Result;
import com.bili.pojo.dto.GetOssStsParam;
import org.springframework.stereotype.Service;

@Service
public interface OssStsService {

    Result getOssStsImage(Long userId, GetOssStsParam getOssStsParam) throws ClientException;

    Result getOssStsVideo(Long userId, String suffix) throws ClientException;

    boolean checkFile(Long userId, String fileName);

    void FinishFileUpload(Long userId, String fileName);
}
