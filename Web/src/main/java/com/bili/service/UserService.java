package com.bili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bili.dto.*;
import com.bili.entity.User;
import com.bili.utils.Result;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserService extends IService<User> {

    /**
     * 验证邮箱验证码
     * @param email
     * @param code
     * @return
     */
    Result verifyEmailCode(String email, String code);

    /**
     * 账号密码登录
     * @param username
     * @param password
     * @return
     */
    Result nameLogin(String username, String password);

    /**
     * 短信登录及注册
     * @param loginDTO
     * @return
     */
    Result phoneLogin(PhoneLoginDTO loginDTO);

    /**
     * 邮箱登录及注册
     * @param email
     * @param code
     * @return
     */
    Result emailLogin(String email, String code);

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
     * @param newPwd
     * @param confirmPwd
     * @return
     */
    Result resetPwdByEmail(String newPwd, String confirmPwd);

    /**
     * 签到
     * @return
     */
    Result sign();

    /**
     * 修改用户简单信息
     * @param userInfoDTO
     * @return
     */
    Result updateUserInfo(EditUserInfoDTO userInfoDTO);

    /**
     * 更换新邮箱，绑定邮箱
     * @param newEmail
     * @return
     */
    Result updateUserEmail(String newEmail);

    /**
     * 更换新手机号，绑定手机号
     * @param newPhone
     * @return
     */
    Result updateUserPhone(String newPhone);

    /**
     * 验证手机验证码
     * @param phone
     * @param code
     * @return
     */
    Result verifyPhoneCode(String phone, String code);
}
