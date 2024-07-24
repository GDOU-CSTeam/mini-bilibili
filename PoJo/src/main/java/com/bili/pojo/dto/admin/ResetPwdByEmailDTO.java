package com.bili.pojo.dto.admin;

import lombok.Data;

@Data
public class ResetPwdByEmailDTO {

    private String newPwd;
    private String confirmPwd;
    private String email;
    private String code;
}
