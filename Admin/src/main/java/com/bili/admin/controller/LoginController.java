package com.bili.admin.controller;

import com.bili.common.utils.Result;
import com.bili.admin.service.LoginService;
import com.bili.pojo.dto.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;


    /**
     * 账号密码登录
     * @param loginDTO
     * @return
     */
    @PostMapping("/login/name")
    public Result nameLogin(@RequestBody NameLoginDTO loginDTO){
        return loginService.nameLogin(loginDTO);
    }

    /**
     * 短信登录及注册
     * @param loginDTO
     * @return
     */
    @PostMapping("/login/phone")
    public Result phoneLogin(@RequestBody PhoneLoginDTO loginDTO){
        return loginService.phoneLogin(loginDTO);
    }

    /**
     * 邮箱登录及注册
     * @param loginDTO
     * @return
     */
    @PostMapping("/login/email")
    public Result emailLogin(@RequestBody EmailLoginDTO loginDTO){
        return loginService.emailLogin(loginDTO);
    }

    /**
     * 用户登出
     * @return
     */
    @DeleteMapping("login/logout")
    public Result logout(){
        return loginService.logout();
    }


    /**
     * 获取手机验证码
     * @param phoneNumber
     * @return
     */
    @GetMapping("/login/phone_code")
    public Result sendPhoneCode(@RequestParam String phoneNumber){
        return loginService.sendPhoneCode(phoneNumber);
    }

    /**
     * 获取邮箱验证码
     * @param email
     * @return
     */
    @GetMapping("/login/email_code")
    public Result sendEmailCode(@RequestParam String email){
        return loginService.sendEmailCode(email);
    }


    /**
     * 用手机短信重置密码
     * @param resetPwdDTO
     * @return
     */
    @PostMapping("/login/reset_pwd_by_phone")
    public Result resetPwdByPhone(@RequestBody ResetPwdByPhoneDTO resetPwdDTO){
        return loginService.resetPwdByPhone(resetPwdDTO);
    }

    /**
     * 用邮箱验证码重置密码
     * @param resetPwdDTO
     * @return
     */
    @PostMapping("/login/reset_pwd_by_emil")
    public Result resetPwdByEmail(@RequestBody ResetPwdByEmailDTO resetPwdDTO){
        return loginService.resetPwdByEmail(resetPwdDTO);
    }

    /**
     * 注销账号
     * @return
     */
    @DeleteMapping("/login/sign_out")
    public Result signOut(){
        //还没写
        return Result.failed("还没写");
    }
}
