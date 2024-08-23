package com.bili.controller;

import com.bili.dto.*;
import com.bili.service.UserService;
import com.bili.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "用户登录")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login/name")
    @Operation(summary = "账号密码登录")
    public Result nameLogin(@RequestParam String username, @RequestParam String password){
        return userService.nameLogin(username, password);
    }


    //@PostMapping("/login/phone")
    //@Operation(summary = "短信登录及注册")
    public Result phoneLogin(@RequestBody PhoneLoginDTO loginDTO){
        return userService.phoneLogin(loginDTO);
    }

    @PostMapping("/login/email")
    @Operation(summary = "邮箱登录及注册")
    public Result emailLogin(@RequestParam String email, @RequestParam String code){
        return userService.emailLogin(email,code);
    }


    @DeleteMapping("login/logout")
    @Operation(summary = "用户登出")
    public Result logout(){
        return userService.logout();
    }


    @GetMapping("/login/get_phone_code")
    @Operation(summary = "获取手机验证码")
    public Result sendPhoneCode(@RequestParam String phoneNumber){
        return userService.sendPhoneCode(phoneNumber);
    }

    @GetMapping("/login/get_email_code")
    @Operation(summary = "获取邮箱验证码")
    public Result sendEmailCode(@RequestParam String email){
        return userService.sendEmailCode(email);
    }

    @PostMapping("/login/verify_email_code")
    @Operation(summary = "验证邮箱验证码")
    public Result verifyEmailCode(@RequestParam String email, @RequestParam String code){
        return userService.verifyEmailCode(email,code);
    }

    @PostMapping("/login/verify_phone_code")
    @Operation(summary = "验证手机验证码")
    public Result verifyPhoneCode(@RequestParam String phone, @RequestParam String code){
        return userService.verifyPhoneCode(phone,code);
    }


    //@PostMapping("/login/reset_pwd_by_phone")
    //@Operation(summary = "用手机短信重置密码")
    public Result resetPwdByPhone(@RequestBody ResetPwdByPhoneDTO resetPwdDTO){
        return userService.resetPwdByPhone(resetPwdDTO);
    }

    @Operation(summary = "重置密码")
    @PostMapping("/login/reset_pwd_by_emil")
    public Result resetPwdByEmail(@RequestParam String newPwd, @RequestParam String confirmPwd){
        return userService.resetPwdByEmail(newPwd, confirmPwd);
    }

    //@DeleteMapping("/login/sign_out")
    //@Operation(summary = "注销账号")
    public Result signOut(){
        //还没写
        return Result.failed("还没写");
    }

    @Operation(summary = "签到")
    @PostMapping("/sign")
    public Result sign(){
        return userService.sign();
    }
}
