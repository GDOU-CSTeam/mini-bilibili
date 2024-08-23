package com.bili.controller;

import com.bili.dto.AddVideoDTO;
import com.bili.dto.EditVideoDTO;
import com.bili.dto.VideoPageQueryDTO;
import com.bili.entity.Video;
import com.bili.service.VideoService;
import com.bili.utils.PageInfo;
import com.bili.utils.Result;
import com.bili.utils.UserDTOHolder;
import com.bili.vo.VideoPageVO;
import com.bili.vo.VideoVO;
import com.rabbitmq.client.Return;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name="视频相关接口")
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 视频上传
     * @param file
     * @return
     */
    //@PostMapping("/upload")
    public Result uploadVideo(MultipartFile file){
        log.info("视频上传：{}", file);
        //1.获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //2.获取后缀
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //3.构建新的文件名
        String newFileName = UUID.randomUUID().toString() + extension;
        //TODO
        return Result.success();
    }


    /**
     * 添加视频
     * @param addVideoDTO
     * @return
     */
    @PostMapping("/add")
    @Operation(summary = "添加视频")
    public Result addVideo(@RequestBody AddVideoDTO addVideoDTO){
        log.info("添加视频:{}",addVideoDTO);
        videoService.addVideo(addVideoDTO);
        return Result.success();
    }

    /**
     * 根据id查询视频
     * @param id
     * @return
     */
    @Operation(summary = "根据id查询视频")
    @GetMapping("/{id}")
    public Result getVideoById(@PathVariable Integer id){
        log.info("根据id查询视频:{}",id);
        VideoVO videoVO = videoService.getVideoById(id);
        return Result.success(videoVO);
    }


    /**
     * 批量删除视频
     * @param ids
     * @return
     */
    @Operation(summary = "批量删除视频")
    @DeleteMapping("/{ids}")
    public Result deleteVideoByIds(@PathVariable List<Integer> ids){
        log.info("批量删除视频:{}",ids);
        videoService.deleteVideoByIds(ids);
        return Result.success();
    }

    /**
     * 批量查询视频
     * @param ids
     * @return
     */
    @Operation(summary = "批量查询视频")
    @GetMapping("/batch/{ids}")
    public Result getVideoByIds(@PathVariable List<Integer> ids){
        return videoService.getVideoByIds(ids);
    }


    /**
     * 分页查询视频
     * @param pageQueryDTO
     * @return
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询视频")
    public Result pageVideo(@RequestBody VideoPageQueryDTO pageQueryDTO){
        log.info("分页查询视频:{}",pageQueryDTO);
        PageInfo<VideoPageVO> pageInfo = videoService.pageVideo(pageQueryDTO);
        return Result.success(pageInfo);
    }

    /**
     * 修改视频信息
     * @param editVideoDTO
     * @return
     */
    @PutMapping("/edit")
    @Operation(summary = "修改视频信息")
    public Result editVideo(@RequestBody EditVideoDTO editVideoDTO){
        log.info("修改视频信息：{}",editVideoDTO);
        videoService.editVideo(editVideoDTO);
        return Result.success();
    }

    /**
     * 视频点赞与取消
     * @param videoId
     * @return
     */
    @PutMapping("/like/{videoId}")
    @Operation(summary = "视频点赞与取消")
    public Result likeVideo(@PathVariable Long videoId){
        return videoService.likeVideo(videoId);
    }

    /**
     * 视频投币
     * @param videoId
     * @return
     */
    @PutMapping("/coin/{videoId}")
    @Operation(summary = "视频投币")
    public Result coinVideo(@PathVariable long videoId, @RequestParam Integer count){
        return videoService.coinVideo(videoId, count);
    }

    /**
     * 视频收藏与取消
     * @param videoId
     * @return
     */
    @PostMapping("/collection/{videoId}")
    @Operation(summary = "视频收藏与取消")
    public Result collectionVideo(@PathVariable Long videoId, @RequestParam List<Integer> collectionIds){
        log.info("视频收藏与取消:{},{}",videoId,collectionIds);
        return videoService.collectionVideo(videoId, collectionIds);
    }

    /**
     * 视频分享
     * @param videoId
     * @return
     */
    @GetMapping("/share/{videoId}")
    @Operation(summary = "视频分享")
    public Result shareVideo(@PathVariable Long videoId){
        return videoService.shareVideo(videoId);
    }

    @Operation(summary = "查询是否点赞投币收藏")
    @GetMapping("/operation/{videoId}")
    public Result getMyOperation(@PathVariable Long videoId){
        return videoService.getMyOperation(videoId);
    }

    @Operation(summary = "查询是否是今天第一次观看视频")
    @GetMapping("/first-view")
    public Result isFirstVideoView(){
        return videoService.isFirstVideoView();
    }
}
