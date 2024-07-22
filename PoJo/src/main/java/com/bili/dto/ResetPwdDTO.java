package com.bili.dto;

import lombok.Data;

@Data
public class ResetPwdDTO {

    private String newPwd;
    private String confirmPwd;
    private String phone;
    private String code;
}
