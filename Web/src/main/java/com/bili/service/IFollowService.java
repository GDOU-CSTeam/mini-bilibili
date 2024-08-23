package com.bili.service;

import com.bili.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bili.utils.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-21
 */
public interface IFollowService extends IService<Follow> {

    /**
     * 关注或取关
     * @param followUserId
     * @param isFollow
     * @return
     */
    Result follow(Long followUserId, Boolean isFollow);

    /**
     * 是否关注用户
     * @param followUserId
     * @return
     */
    Result isFollow(Long followUserId);

    /**
     * 共同关注
     * @param followUserId
     * @return
     */
    Result followCommons(Long followUserId);
}
