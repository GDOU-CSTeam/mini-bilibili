package com.bili.web.task;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bili.common.utils.MediaUtil;
import com.bili.common.utils.ModerationUtil;
import com.bili.common.utils.Mp4VideoUtil;
import com.bili.pojo.entity.MediaFiles;
import com.bili.pojo.entity.MediaProcess;
import com.bili.web.service.MediaFileProcessService;
import com.bili.web.service.MediaFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;


/**
 * 任务处理类
 */
@Slf4j
@Component
public class MediaTask {

    @Autowired
    private MediaFileProcessService mediaFileProcessService;

    @Autowired
    private MediaFileService mediaFileService;

    @Autowired
    private MediaUtil mediaUtil;

    @Autowired
    private ModerationUtil moderationUtil;

    // 确定任务线程数
    private int ScheduledCount = 3;

    // ffmpeg的路径
    @Value("${videoprocess.ffmpegpath}")
    private String ffmpegpath;

    /**
     * 视频处理任务0，每分钟的第20秒执行
     */
    @Scheduled(cron = "20 * * * * ?")
    public void videoJobHandler0() {
        processMedia(0);  // 执行任务1
    }

    /**
     * 视频处理任务1，每分钟的第40秒执行
     */
    @Scheduled(cron = "40 * * * * ?")
    public void videoJobHandler1() {
        processMedia(1);  // 执行任务2
    }

    /**
     * 视频处理任务2，每分钟的第60秒执行（即每分钟结束时）
     */
    @Scheduled(cron = "0 * * * * ?")
    public void videoJobHandler2() {
        processMedia(2);  // 执行任务3
        // 获取审核任务结果
        moderationVideo();
    }

    private void processMedia(int scheduledId) {
        // 查询待处理的任务
        List<MediaProcess> mediaProcessList = mediaFileProcessService.getMediaProcessList(scheduledId, ScheduledCount, 1);
        // 任务数量
        int size = mediaProcessList.size();
        log.info(Thread.currentThread().getName(), Thread.currentThread().getId());
        log.info("取到视频转码任务数: {}", size);
        if (size <= 0) {
            return;
        }
        mediaProcessList.forEach(mediaProcess -> {
            // 任务id
            Long taskId = mediaProcess.getId();
            // 文件id就是md5
            String fileId = mediaProcess.getFileId();
            // 开启任务
            boolean b = mediaFileProcessService.startTask(taskId);
            if (!b) {
                log.debug("抢占任务失败,任务id:{}", taskId);
                return;
            }

            // 桶
            String bucket = mediaProcess.getBucket();
            // objectName
            String objectName = mediaProcess.getFilePath();

            // 下载minio视频到本地
            File file = mediaFileService.downloadFileFromMinIO(bucket, objectName);
            if (file == null) {
                log.debug("下载视频出错,任务id:{},bucket:{},objectName:{}", taskId, bucket, objectName);
                // 保存任务处理失败的结果
                mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "下载视频到本地失败");
                return;
            }

            // 源avi视频的路径
            String video_path = file.getAbsolutePath();
            // 转换后mp4文件的名称
            String mp4_name = fileId + ".mp4";
            // 转换后mp4文件的路径
            // 先创建一个临时文件，作为转换后的文件
            File mp4File = null;
            try {
                mp4File = File.createTempFile("minio", ".mp4");
            } catch (IOException e) {
                log.debug("创建临时文件异常,{}", e.getMessage());
                // 保存任务处理失败的结果
                mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "创建临时文件异常");
                return;
            }
            String mp4_path = mp4File.getAbsolutePath();
            // 创建工具类对象
            Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpegpath, video_path, mp4_name, mp4_path);
            // 开始视频转换，成功将返回success,失败返回失败原因
            String result = videoUtil.generateMp4();
            if (!result.equals("success")) {
                log.debug("视频转码失败,原因:{},bucket:{},objectName:{},", result, bucket, objectName);
                mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, result);
                return;
            }

            // 上传到minio
            boolean b1 = mediaFileService.addMediaFilesToMinIO(mp4_path, bucket, mp4_name);
            if (!b1) {
                log.debug("上传mp4到minio失败,taskid:{}", taskId);
                mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "上传mp4到minio失败");
                return;
            }

            // mp4文件的url
            String url = bucket + "/" + getFilePath(fileId, ".mp4");

            // 更新任务状态为成功
            mediaFileProcessService.saveProcessFinishStatus(taskId, "2", fileId, url, "任务成功");
        });
    }



    private void moderationVideo(){
         // 查询处理中的任务
        List<MediaFiles> mediaList = mediaFileService.getMediaToBeModerationList(1);
        // 任务数量
        int size = mediaList.size();
        log.info(Thread.currentThread().getName(), Thread.currentThread().getId());
        log.info("取到视频审核任务数: {}", size);
        if (size <= 0) {
            return;
        }

        mediaList.forEach(mediaFiles -> {
            //任务url
            String taskId = mediaFiles.getRemark();
            String result = moderationUtil.videoModerateSecond(taskId);
            if(result.equals("SUCCESS")){
                //更新状态未审核通过
                mediaFiles.setAuditStatus("002004");
                mediaFileService.updateById(mediaFiles);
            }else if(result.equals("PROCESSING")){
                // 不做任何操作
            }else {
                //更新状态未审核不通过
                mediaFiles.setAuditStatus("002001");
                mediaFileService.updateById(mediaFiles);
            }
        });
    }


    private String getFilePath(String fileMd5, String fileExt) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
    }
}
