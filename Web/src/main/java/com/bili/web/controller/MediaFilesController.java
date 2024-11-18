package com.bili.web.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.green20220302.models.ImageModerationResponseBody;
import com.aliyun.green20220302.models.VideoModerationResponseBody;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bili.common.exception.BiliLikeException;
import com.bili.common.utils.MediaUtil;
import com.bili.common.utils.ModerationUtil;
import com.bili.common.utils.Result;
import com.bili.pojo.dto.*;
import com.bili.pojo.entity.MediaFiles;
import com.bili.web.service.MediaFileService;
import io.micrometer.core.instrument.Metrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Mr.M
 * @version 1.0
 * @description 媒资文件管理接口
 * @date 2022/9/6 11:29
 */
@Tag(name = "媒资文件管理接口")
@RestController
public class MediaFilesController {


    @Autowired
    private MediaFileService mediaFileService;
    @Autowired
    private ModerationUtil moderationUtil;
    @Autowired
    private MediaUtil mediaUtil;
    @Value("${minio.endpoint}")
    private String endpoint;


    @Operation(summary = "媒资列表查询接口")
    @PostMapping("/files")
    public PageResult<MediaFiles> list(Long userId, PageParams pageParams, @RequestBody QueryMediaParamsDto queryMediaParamsDto) {
        return mediaFileService.queryMediaFiles(userId, pageParams, queryMediaParamsDto);

    }


    @Operation(summary ="上传文件")
    @PostMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResultDto upload(@RequestPart("filedata") MultipartFile upload,
                                      @RequestParam(value = "folder", required = false) String folder,
                                      @RequestParam(value = "objectName", required = false) String objectName) {
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFileSize(upload.getSize());
        String contentType = upload.getContentType();
        if (contentType.contains("image")) {
            uploadFileParamsDto.setFileType("001001");
        } else {
            uploadFileParamsDto.setFileType("001003");
        }
        uploadFileParamsDto.setRemark("");
        uploadFileParamsDto.setFilename(upload.getOriginalFilename());
        uploadFileParamsDto.setContentType(contentType);
        Long userId = 1232141425L;
        try {
            UploadFileResultDto uploadFileResultDto = mediaFileService.uploadFile(userId, uploadFileParamsDto, upload.getBytes(), folder, objectName);

            // 提交检测任务到异步线程
            processModerationAsync(uploadFileResultDto);

            return uploadFileResultDto;
        } catch (IOException e) {
            BiliLikeException.cast("上传文件过程出错:" + e.getMessage());
        }
        return null;
    }

    // 异步处理视频审核任务
    @Async
    void processModerationAsync(UploadFileResultDto uploadFileResultDto) {
        try {
            // 提交检测任务
            String taskId = moderationUtil.videoModerateFirst(mediaUtil.getTotalUrl(uploadFileResultDto.getUrl()));
            if(StrUtil.isNotEmpty(taskId)){
                boolean update = mediaFileService.update(new LambdaUpdateWrapper<MediaFiles>()
                        .eq(MediaFiles::getId, uploadFileResultDto.getId())
                        //标识审核中
                        .set(MediaFiles::getAuditStatus, "002003")
                        .set(MediaFiles::getRemark, taskId));
            }
        } catch (Exception e) {
            // 处理审核失败的情况
            // 可在此处记录日志，或重试
            e.printStackTrace();
            throw new RuntimeException("视频审核提交失败", e);
        }
    }
}
