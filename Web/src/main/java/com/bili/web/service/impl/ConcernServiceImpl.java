package com.bili.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bili.common.dto.PageSelectWithIdParam;
import com.bili.common.utils.Result;
import com.bili.pojo.entity.user.BConcern;
import com.bili.pojo.mapper.user.BConcernMapper;
import com.bili.pojo.vo.user.UserInfoShowVo;
import com.bili.web.service.ConcernService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ConcernServiceImpl implements ConcernService {

    @Resource
    BConcernMapper bConcernMapper;

    @Override
    public Result concernUser(Long userId, Long concernUserId) {
        //判断是否关注自己
        if (userId.equals(concernUserId)) {
            return Result.failed("不能关注自己");
        }
        //判断是否已经关注
        LambdaQueryWrapper<BConcern> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BConcern::getUserId, userId).eq(BConcern::getConcernUserId, concernUserId);
        BConcern bConcernExist = bConcernMapper.selectOne(queryWrapper);
        if (bConcernExist != null) {
            return Result.failed("已关注该用户");
        }
        BConcern bConcern = new BConcern();
        bConcern.setUserId(userId).setConcernUserId(concernUserId);
        bConcernMapper.insert(bConcern);
        return Result.success("关注成功");
    }

    @Override
    public Result cancelConcern(Long userId, Long concernUserId) {
        //判断是否已关注
        LambdaQueryWrapper<BConcern> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BConcern::getUserId, userId).eq(BConcern::getConcernUserId, concernUserId);
        BConcern bConcernExist = bConcernMapper.selectOne(queryWrapper);
        if (bConcernExist == null) {
            return Result.failed("未关注该用户");
        }
        queryWrapper.clear();
        queryWrapper.eq(BConcern::getUserId, userId).eq(BConcern::getConcernUserId, concernUserId);
        bConcernMapper.delete(queryWrapper);
        return Result.success("取消关注成功");
    }

    @Override
    public Result getConcernList(PageSelectWithIdParam pageSelectWithIdParam) {
        Page<UserInfoShowVo> page = new Page<>(pageSelectWithIdParam.getPage(), pageSelectWithIdParam.getPageSize());
        Page<UserInfoShowVo> bConcernPage = bConcernMapper.selectConcernedByUserId(
                Long.valueOf(pageSelectWithIdParam.getSelectId()), page);
        return Result.success(bConcernPage);
    }

    @Override
    public Result getFansList(PageSelectWithIdParam pageSelectWithIdParam) {
        Page<UserInfoShowVo> page = new Page<>(pageSelectWithIdParam.getPage(), pageSelectWithIdParam.getPageSize());
        Page<UserInfoShowVo> bFansPage = bConcernMapper.selectFansByUserId(
                Long.valueOf(pageSelectWithIdParam.getSelectId()), page);
        return Result.success(bFansPage);
    }
}
