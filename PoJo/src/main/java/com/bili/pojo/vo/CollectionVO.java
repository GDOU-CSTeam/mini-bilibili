package com.bili.pojo.vo;

import com.bili.pojo.entity.VideoCollections;
import lombok.Data;

import java.util.Set;

@Data
public class CollectionVO {

    private VideoCollections videoCollections;

    private Set<String> videoList;

}
