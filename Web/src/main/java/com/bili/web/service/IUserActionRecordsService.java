package com.bili.web.service;

import com.bili.pojo.entity.UserActionRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bili.web.cache.ActionRecord;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hygl
 * @since 2024-12-06
 */
public interface IUserActionRecordsService extends IService<UserActionRecord> {

    /**
     * 把缓存持久化到到数据库
     * @param actionRecord
     * @return
     */
    void savaToDataBase(ActionRecord actionRecord);

    ActionRecord getFormDataBase(Long userId);

}
