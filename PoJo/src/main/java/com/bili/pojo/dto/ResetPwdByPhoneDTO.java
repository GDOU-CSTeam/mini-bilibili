package com.bili.pojo.dto;

import lombok.Data;

@Data
public class ResetPwdByPhoneDTO {

    private String newPwd;
    private String confirmPwd;
    private String phone;
    private String code;
}
