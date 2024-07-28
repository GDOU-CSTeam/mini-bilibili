package com.bili.web.controller;

import com.aliyuncs.exceptions.ClientException;
import com.bili.common.utils.Result;
import com.bili.web.service.UserService;
import com.bili.pojo.dto.user.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统管理-用户基础信息表 前端控制器
 * </p>
 *
 * @author lin
 * @since 2024-07-24 05:19:39
 */
@RestController
@RequestMapping("/User")
@Tag(name = "User", description = "用户信息接口")
@Validated
public class UserController {

    @Resource
    UserService userService;

    //每日签到
    @PostMapping("/sign_in")
    @Operation(summary = "每日签到")
    public Result signIn() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.signIn(Long.valueOf(userId));
    }

    //获取用户信息
    @GetMapping("/get_user_info")
    @Operation(summary = "获取用户信息")
    public Result getUserInfo(@NotBlank @RequestParam Long userId) {
        return userService.getUserInfo(userId);
    }

    @PostMapping("/username_login")
    @Operation(summary = "通过用户名登录")
    public Result login(@RequestBody @Validated UserNameLoginParam userNameLoginParam) {
        return userService.usernameLogin(userNameLoginParam);
    }

    @PostMapping("/email_login")
    @Operation(summary = "通过邮箱登录")
    public Result login(@RequestBody @Validated EmailLoginParam emailLoginParam) {
        return userService.emailLogin(emailLoginParam);
    }


    @PostMapping("/sign")
    @Operation(summary = "用户注册")
    public Result sign(@RequestBody @Validated SignParam signParam) {
        return userService.sign(signParam);
    }

    @GetMapping("/get_code/{email}")
    @Operation(summary = "获取注册验证码")
    public Result getCode(@PathVariable @Email(message = "邮箱格式错误") String email) throws JsonProcessingException, InterruptedException {
        return userService.getCode(email);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/change_password")
    public Result changePassword(@Validated @RequestBody ChangePasswordParam changePasswordParam) {
        return userService.changePassword(changePasswordParam);
    }

    @Operation(summary = "忘记密码")
    @PutMapping("/forget_password")
    public Result forgetPassword(@Validated @RequestBody ForgetPassWordParam forgetPassWordParam) {
        return userService.forgetPassword(forgetPassWordParam);
    }


    @GetMapping("quit")
    @Operation(summary = "退出登录")
    public Result quit() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return userService.quit(userId);
    }

    @GetMapping("/refresh_token")
    @Operation(summary = "刷新token")
    public Result refreshToken() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return userService.refreshToken(userId);
    }

    @PutMapping("/update_user_info")
    @Operation(summary = "更新用户信息")
    public Result updateUserInfo(@RequestBody @Validated UpdateUserInfoParam updateUserInfoParam) {
        return userService.updateUserInfo(updateUserInfoParam);
    }

    @GetMapping("/get_image_sts")
    @Operation(summary = "获取图片上传凭证")
    public Result getImageSts(@RequestParam @NotBlank String suffix) throws ClientException {
        return userService.getImageSts(suffix);
    }
}
