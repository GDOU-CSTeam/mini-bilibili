package com.bili.pojo.dto.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class PhoneLoginDTO implements Serializable {

    private String phoneNumber;
    private String code;
}
