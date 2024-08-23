package com.bili.controller;


import com.bili.dto.EditUserInfoDTO;
import com.bili.entity.UserInfo;
import com.bili.service.UserService;
import com.bili.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-22
 */
@RestController
@RequestMapping("/user-info")
@Tag(name = "用户信息模块")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @PutMapping("/edit_info")
    @Operation(summary = "修改用户简单信息")
    public Result updateUserInfo(@RequestBody EditUserInfoDTO userInfoDTO){
        return userService.updateUserInfo(userInfoDTO);
    }

    @PutMapping("/bind_email")
    @Operation(summary = "更换新邮箱，绑定邮箱")
    public Result updateUserEmail(@RequestParam String newEmail){
        return userService.updateUserEmail(newEmail);
    }

    @PutMapping("/bind_phone")
    @Operation(summary = "更换新手机号，绑定手机号")
    public Result updateUserPhone(@RequestParam String newPhone){
        return userService.updateUserPhone(newPhone);
    }



}
