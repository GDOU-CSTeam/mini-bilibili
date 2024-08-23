package com.bili.vo;

import com.bili.entity.VideoCollections;
import lombok.Data;

import java.util.Set;

@Data
public class CollectionVO {

    private VideoCollections videoCollections;

    private Set<String> videoList;

}
