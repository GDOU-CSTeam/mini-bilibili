package com.sky.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SnowFlake {
    @Value("${snowflake.workerId}")
    int workerId;
    @Value("${snowflake.datacenterId}")
    int datacenterId;
    Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);

    public Long getId(){
        return snowflake.nextId();
    }
}
