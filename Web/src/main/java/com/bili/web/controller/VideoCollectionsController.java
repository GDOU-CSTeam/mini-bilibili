package com.bili.web.controller;


import com.bili.common.utils.Result;
import com.bili.pojo.entity.ValidGroup;
import com.bili.pojo.entity.VideoCollections;
import com.bili.pojo.vo.CollectionVO;
import com.bili.web.service.IVideoCollectionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 视频收藏夹表 前端控制器
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-15
 */
@Slf4j
@RestController
@RequestMapping("/video-collections")
@Tag(name = "视频收藏模块")
public class VideoCollectionsController {

    @Autowired
    private IVideoCollectionsService collectionsService;

    @Operation(summary = "添加收藏夹")
    @PostMapping()
    public Result addCollection(@Validated(value = ValidGroup.Create.class)
                                    @RequestBody VideoCollections collectionDTO){
        log.info("添加收藏夹:{}",collectionDTO );
        return collectionsService.addCollection(collectionDTO);
    }

    @Operation(summary = "根据id查询收藏夹")
    @GetMapping("/{collectionId}")
    public Result getCollectionById(@PathVariable Integer collectionId){
        CollectionVO collectionVO = collectionsService.getCollectionById(collectionId);
        return Result.success(collectionVO);
    }

    @Operation(summary = "获取用户所有收藏夹")
    @GetMapping("/list")
    public Result getCollectionList(){
        return collectionsService.getCollectionList();
    }

    @Operation(summary = "删除收藏夹")
    @DeleteMapping("/{collectionId}")
    public Result deleteCollection(@PathVariable Integer collectionId){
        return collectionsService.deleteCollection(collectionId);
    }

    @Operation(summary = "修改收藏夹")
    @PutMapping()
    public Result updateCollection(@Validated(value = ValidGroup.Update.class)
                                       @RequestBody VideoCollections videoCollections){
        return collectionsService.updateCollection(videoCollections);
    }
}
