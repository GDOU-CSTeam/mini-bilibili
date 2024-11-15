package com.bili.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bili.common.dto.PageSelectWithIdParam;
import com.bili.common.utils.Result;
import com.bili.pojo.entity.BConcern;
import com.bili.pojo.entity.User;
import com.bili.pojo.entity.UserInfo;
import com.bili.web.mapper.BConcernMapper;
import com.bili.web.mapper.UserInfoMapper;
import com.bili.web.mapper.UserMapper;
import com.bili.pojo.vo.UserInfoShowVo;
import com.bili.web.service.ConcernService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ConcernServiceImpl implements ConcernService {

    @Resource
    BConcernMapper bConcernMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    UserMapper userMapper;
    @Resource
    UserInfoMapper userInfoMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
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
        stringRedisTemplate.opsForSet().add("follow:" + concernUserId, userId.toString());
        return Result.success("关注成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        stringRedisTemplate.opsForSet().remove("follow:" + concernUserId, userId.toString());
        return Result.success("取消关注成功");
    }

    @Override
    public Result getConcernList(PageSelectWithIdParam pageSelectWithIdParam) {
        Page<UserInfoShowVo> page = new Page<>(pageSelectWithIdParam.getPage(), pageSelectWithIdParam.getPageSize(),
                false);
        Page<UserInfoShowVo> bConcernPage = bConcernMapper.selectConcernedByUserId(
                Long.valueOf(pageSelectWithIdParam.getSelectId()), page);
        return Result.success(bConcernPage.getRecords());
    }

    @Override
    public Result getFansList(PageSelectWithIdParam pageSelectWithIdParam) {
        Page<UserInfoShowVo> page = new Page<>(pageSelectWithIdParam.getPage(), pageSelectWithIdParam.getPageSize(),
                false);
        Page<UserInfoShowVo> bFansPage = bConcernMapper.selectFansByUserId(
                Long.valueOf(pageSelectWithIdParam.getSelectId()), page);
        return Result.success(bFansPage.getRecords());
    }

    @Override
    public Result getConcernStatus(Long userId, Long concernUserId) {
        LambdaQueryWrapper<BConcern> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BConcern::getUserId, userId).eq(BConcern::getConcernUserId, concernUserId);
        BConcern bConcern = bConcernMapper.selectOne(queryWrapper);
        if (bConcern != null) {
            return Result.success(true);
        }
        return Result.success(false);
    }

    @Override
    public Result getCommonConcernList(Long userId, Long concernUserId) {
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect("follow:" + userId,
                "follow:" + concernUserId);
        if (intersect == null || intersect.isEmpty()) {
            return Result.success(null);
        }
        List<String> userList = new ArrayList<>(intersect);
        List<User> users = userMapper.selectBatchIds(userList);
        List<UserInfo> userInfos = userInfoMapper.selectBatchIds(userList);
        List<UserInfoShowVo> userInfoShowVos = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            UserInfoShowVo userInfoShowVo = new UserInfoShowVo();
            userInfoShowVo.setId(users.get(i).getId());
            userInfoShowVo.setNickname(users.get(i).getNickName());
            userInfoShowVo.setAvatar(users.get(i).getAvatar());
            userInfoShowVo.setIntro(userInfos.get(i).getIntro());
            userInfoShowVos.add(userInfoShowVo);
        }
        return Result.success(userInfoShowVos);
    }
}
