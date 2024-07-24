package com.bili.pojo.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameLoginDTO implements Serializable {

    private String userName;
    private String password;
}
