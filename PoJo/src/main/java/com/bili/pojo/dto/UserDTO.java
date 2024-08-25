package com.bili.pojo.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String userName;
    private String nickName;
    private String avatar;
    private String password;
}
