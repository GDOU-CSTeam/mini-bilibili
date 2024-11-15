package com.bili.common.utils;



import com.bili.pojo.dto.UserDTO;
import com.bili.pojo.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserDTOHolder {
    public static UserDTO getUserDTO(){
        //获取用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        UserDTO userDTO = loginUser.getUser();
        return userDTO;
    }
}
