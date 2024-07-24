package com.bili.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bili.pojo.dto.admin.*;
import com.bili.pojo.entity.admin.User;
import com.bili.common.utils.Result;

public interface LoginService extends IService<User> {

    /**
     * 账号密码登录
     * @param loginDTO
     * @return
     */
    Result nameLogin(NameLoginDTO loginDTO);

    /**
     * 短信登录及注册
     * @param loginDTO
     * @return
     */
    Result phoneLogin(PhoneLoginDTO loginDTO);

    /**
     * 邮箱登录及注册
     * @param loginDTO
     * @return
     */
    Result emailLogin(EmailLoginDTO loginDTO);

    /**
     * 用户登出
     * @return
     */
    Result logout();

    /**
     * 发送手机短信验证码
     * @param phone
     * @return
     */
    Result sendPhoneCode(String phone);


    /**
     * 手机验证码重置密码
     * @param resetPwdDTO
     * @return
     */
    Result resetPwdByPhone(ResetPwdByPhoneDTO resetPwdDTO);


    /**
     *  发送邮箱验证码
     * @param email
     * @return
     */
    Result sendEmailCode(String email);


    /**
     * 邮箱验证码重置密码
     * @param resetPwdDTO
     * @return
     */
    Result resetPwdByEmail(ResetPwdByEmailDTO resetPwdDTO);
}
