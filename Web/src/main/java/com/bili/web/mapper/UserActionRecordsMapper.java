package com.bili.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bili.pojo.entity.ProductUserBitmap;
import com.bili.pojo.entity.UserActionRecord;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hygl
 * @since 2024-12-06
 */
public interface UserActionRecordsMapper extends BaseMapper<UserActionRecord> {

    @Select("select * from user_action_records where user_id = #{userId}")
    @Results({
            @Result(column = "action_data", property = "actionData" , jdbcType = JdbcType.BLOB, typeHandler = org.apache.ibatis.type.BlobTypeHandler.class)
    })
    List<UserActionRecord> getUserRecords(Long userId);



    // 保存 RoaringBitmap 到数据库
    @Insert("INSERT INTO user_action_records (user_id, action_type, action_data) VALUES (#{userId}, #{actionType}, #{actionData, jdbcType=BLOB})")
    void saveRoaringBitmap(UserActionRecord userActionRecord);

}
