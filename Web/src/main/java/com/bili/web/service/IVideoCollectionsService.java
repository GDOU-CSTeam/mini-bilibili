package com.bili.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bili.common.utils.Result;
import com.bili.pojo.entity.VideoCollections;
import com.bili.pojo.vo.CollectionVO;

/**
 * <p>
 * 视频收藏夹表 服务类
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-15
 */
public interface IVideoCollectionsService extends IService<VideoCollections> {

    /**
     * 添加收藏夹
     * @param collectionDTO
     * @return
     */
    Result addCollection(VideoCollections collectionDTO);

    /**
     * 根据id查询收藏夹
     * @param collectionId
     * @return
     */
    CollectionVO getCollectionById(Integer collectionId);

    /**
     * 获取用户所有收藏夹
     * @return
     */
    Result getCollectionList();

    /**
     * 删除收藏夹
     * @param collectionId
     * @return
     */
    Result deleteCollection(Integer collectionId);

    /**
     * 修改收藏夹
     * @param videoCollections
     * @return
     */
    Result updateCollection(VideoCollections videoCollections);
}
