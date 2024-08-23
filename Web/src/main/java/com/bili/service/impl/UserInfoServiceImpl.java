package com.bili.service.impl;

import com.bili.entity.UserInfo;
import com.bili.mapper.UserInfoMapper;
import com.bili.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
