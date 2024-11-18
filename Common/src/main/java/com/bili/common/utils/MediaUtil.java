package com.bili.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class MediaUtil {

    private String endpoint = "http://159.75.174.133:9000";

     /**
     * 获取完整访问地址
     * @param url
     * @return
     */
    public String getTotalUrl(String url){
        return endpoint + url;
    }
}
