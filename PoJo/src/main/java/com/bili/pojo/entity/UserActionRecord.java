package com.bili.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Blob;
import java.io.Serializable;

import io.lettuce.core.StrAlgoArgs;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;

/**
 * <p>
 * 
 * </p>
 *
 * @author hygl
 * @since 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_action_records")
@Schema(name="UserActionRecords对象", description="")
public class UserActionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Byte actionType;

    private byte[] actionData;
}
