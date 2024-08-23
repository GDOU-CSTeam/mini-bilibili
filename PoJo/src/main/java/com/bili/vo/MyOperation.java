package com.bili.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class MyOperation {

    private Boolean isLike;

    private Boolean isCoin;

    private Boolean isCollect;
}
