package com.bili.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bili.pojo.entity.UserInfo;
import com.bili.pojo.mapper.UserInfoMapper;
import com.bili.web.service.IUserInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-22
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
