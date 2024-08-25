package com.bili.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Sse传输对象")
public class SseVo {

    @Schema(description = "传输类型 Dynamic为动态通知 Comment为评论通知 Like为点赞通知 Chat为聊天通知")
    String type;

    @Schema(description = "传输数据")
    String data;
}
