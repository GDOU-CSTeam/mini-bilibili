package com.bili.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bili.common.dto.PageSelectWithIdParam;
import com.bili.common.utils.Result;
import com.bili.pojo.entity.user.BConcern;
import com.bili.pojo.mapper.user.BConcernMapper;
import com.bili.web.service.ConcernService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ConcernServiceImpl implements ConcernService {

    @Resource
    BConcernMapper bConcernMapper;

    @Override
    public Result concernUser(Long userId, Long concernUserId) {
        BConcern bConcern = new BConcern();
        bConcern.setUserId(userId).setConcernUserId(concernUserId);
        bConcernMapper.insert(bConcern);
        return Result.success("关注成功");
    }

    @Override
    public Result cancelConcern(Long userId, Long concernUserId) {
        LambdaQueryWrapper<BConcern> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BConcern::getUserId, userId).eq(BConcern::getConcernUserId, concernUserId);
        bConcernMapper.delete(queryWrapper);
        return Result.success("取消关注成功");
    }

    @Override
    public Result getConcernList(PageSelectWithIdParam pageSelectWithIdParam) {
        Page<BConcern> page = new Page<>(pageSelectWithIdParam.getPage(), pageSelectWithIdParam.getPageSize());
        LambdaQueryWrapper<BConcern> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BConcern::getUserId, pageSelectWithIdParam.getSelectId());
        Page<BConcern> bConcernPage = bConcernMapper.selectPage(page, queryWrapper);
        return Result.success(bConcernPage);
    }
}
