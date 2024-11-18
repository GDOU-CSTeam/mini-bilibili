package com.bili.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bili.pojo.dto.*;
import com.bili.pojo.entity.MediaFiles;
import com.bili.pojo.entity.MediaProcess;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface MediaFileService extends IService<MediaFiles> {

    /**
     * @param pageParams          分页参数
     * @param queryMediaParamsDto 查询条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
     */
    PageResult<MediaFiles> queryMediaFiles(Long userId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);


    /**
     * @param userId           用户id
     * @param uploadFileParamsDto 文件信息
     * @param bytes               文件字节数组
     * @param folder              桶下边的子目录
     * @param objectName          对象名称
     * @return com.xuecheng.media.model.dto.UploadFileResultDto
     */
    UploadFileResultDto uploadFile(Long userId, UploadFileParamsDto uploadFileParamsDto, byte[] bytes, String folder, String objectName);

    /**
     * 将文件信息添加到文件表
     *
     * @param userId           用户id
     * @param uploadFileParamsDto 上传文件的信息
     * @param objectName          对象名称
     * @param fileMD5             文件md5码
     * @param bucket              桶
     */
    MediaFiles addMediaFilesToDB(Long userId, UploadFileParamsDto uploadFileParamsDto, String objectName, String fileMD5, String bucket);

    /**
     * 检查文件是否存在
     *
     * @param fileMd5 文件的md5
     * @return
     */
    Boolean checkFile(String fileMd5);

    /**
     * 检查分块是否存在
     *
     * @param fileMd5    文件的MD5
     * @param chunkIndex 分块序号
     * @return
     */
    Boolean checkChunk(String fileMd5, int chunkIndex);

    /**
     * 上传分块
     *
     * @param fileMd5 文件MD5
     * @param chunk   分块序号
     * @param bytes   文件字节
     * @return
     */
    Boolean uploadChunk(String fileMd5, int chunk, byte[] bytes);

    /**
     * 合并分块
     *
     * @param userId               id
     * @param fileMd5             文件MD5
     * @param chunkTotal          分块数量
     * @param uploadFileParamsDto 文件信息
     * @return
     */
    Boolean mergeChunks(Long userId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) throws IOException;

    MediaFiles getFileById(String mediaId);

    /**
     * 从minio下载文件
     * @param bucket        minio中的桶
     * @param objectName    minio中的对象名称
     * @return
     */
    File downloadFileFromMinIO(String bucket, String objectName);

    /**
     * 将本地文件上传到MinIO
     * @param filePath      本地文件路径
     * @param bucket        上传到的桶
     * @param objectName    上传到的objectName
     */
    Boolean addMediaFilesToMinIO(String filePath, String bucket, String objectName);

    /**
     * 根据文件md5，生成在minio中的文件路径
     * @param fileMd5       文件md5
     * @param extension     文件后缀名
     * @return
     */
    String getFilePathByMd5(String fileMd5, String extension);

    /**
     * 获取待审核视频
     * @param count 要获取的数量
     * @return
     */
    List<MediaFiles> getMediaToBeModerationList(int count);

}
