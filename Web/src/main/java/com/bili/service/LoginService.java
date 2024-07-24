package com.bili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bili.dto.NameLoginDTO;
import com.bili.dto.PhoneLoginDTO;
import com.bili.dto.ResetPwdDTO;
import com.bili.entity.User;
import com.bili.utils.Result;

public interface LoginService extends IService<User> {

    /**
     * 账号密码登录
     * @param loginDTO
     * @return
     */
    Result login(NameLoginDTO loginDTO);

    /**
     * 用户登出
     * @return
     */
    Result logout();

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    Result sendCode(String phone);

    /**
     * 短信登录及注册
     * @param loginDTO
     * @return
     */
    Result login(PhoneLoginDTO loginDTO);

    /**
     * 重置密码
     * @param resetPwdDTO
     * @return
     */
    Result resetPassword(ResetPwdDTO resetPwdDTO);


}
