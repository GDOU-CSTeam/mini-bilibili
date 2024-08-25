package com.bili.web.mq.bo;

import com.bili.web.mq.enums.MqBaseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "消息内部基础传输对象")
@Data
@Accessors(chain = true)
public class MqBaseBo {

    @Schema(description = "操作类型")
    MqBaseType type;

    @Schema(description = "消息内容")
    String content;
}
