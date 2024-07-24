package com.bili.controller;

import com.bili.dto.NameLoginDTO;
import com.bili.dto.PhoneLoginDTO;
import com.bili.dto.ResetPwdDTO;
import com.bili.entity.User;
import com.bili.service.LoginService;
import com.bili.utils.Result;
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
    @PostMapping("/user/name_login")
    public Result login(@RequestBody NameLoginDTO loginDTO){
        return loginService.login(loginDTO);
    }

    /**
     * 短信登录
     * @param loginDTO
     * @return
     */
    @PostMapping("/user/phone_login")
    public Result login(@RequestBody PhoneLoginDTO loginDTO){
        return loginService.login(loginDTO);
    }

    /**
     * 用户登出
     * @return
     */
    @DeleteMapping("/user/logout")
    public Result logout(){
        return loginService.logout();
    }


    /**
     * 获取手机验证码
     * @param phoneNumber
     * @return
     */
    @GetMapping("/login/code")
    public Result sendPhoneCode(@RequestParam String phoneNumber){
        return loginService.sendCode(phoneNumber);
    }


    /**
     * 重置密码
     * @param resetPwdDTO
     * @return
     */
    @PostMapping("/login/reset_password")
    public Result resetPassword(@RequestBody ResetPwdDTO resetPwdDTO){
        return loginService.resetPassword(resetPwdDTO);
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
