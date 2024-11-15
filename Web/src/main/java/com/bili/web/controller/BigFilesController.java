package com.bili.web.controller;

import com.bili.common.utils.Result;
import com.bili.pojo.dto.UploadFileParamsDto;
import com.bili.web.service.MediaFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "大文件上传接口")
@RestController
public class BigFilesController {

    @Autowired
    private MediaFileService mediaFileService;

    @Operation(summary = "文件上传前检查文件")
    @PostMapping("/upload/checkfile")
    public Result checkFile(@RequestParam("fileMd5") String fileMd5) {
        Boolean isExist = mediaFileService.checkFile(fileMd5);
        return Result.success(isExist);
    }

    @Operation(summary = "分块文件上传前检查分块")
    @PostMapping("/upload/checkchunk")
    public Result checkChunk(@RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") int chunk) {
        Boolean isExist = mediaFileService.checkChunk(fileMd5, chunk);
        return Result.success(isExist);
    }

    @Operation(summary = "上传分块文件")
    @PostMapping("/upload/uploadchunk")
    public Result uploadChunk(@RequestParam("file") MultipartFile file, @RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") int chunk) throws Exception {
        Boolean isUpload = mediaFileService.uploadChunk(fileMd5, chunk, file.getBytes());
        return isUpload ? Result.success() : Result.failed("上传文件失败");
    }

    @Operation(summary = "合并分块文件")
    @PostMapping("/upload/mergechunks")
    public Result mergeChunks(@RequestParam("fileMd5") String fileMd5, @RequestParam("fileName") String fileName, @RequestParam("chunkTotal") int chunkTotal) throws IOException {
        Long userId = 1232141425L;
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();

        uploadFileParamsDto.setFileType("001002");
        uploadFileParamsDto.setTags("课程视频");
        uploadFileParamsDto.setRemark("");
        uploadFileParamsDto.setFilename(fileName);
        Boolean isMerge = mediaFileService.mergeChunks(userId, fileMd5, chunkTotal, uploadFileParamsDto);
        return isMerge ? Result.success() :Result.failed();
    }
}
