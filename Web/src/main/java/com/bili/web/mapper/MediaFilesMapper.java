package com.bili.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bili.pojo.entity.MediaFiles;
import com.bili.pojo.entity.MediaProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 媒资信息 Mapper 接口
 * </p>
 *
 * @author itcast
 */
@Mapper
public interface MediaFilesMapper extends BaseMapper<MediaFiles> {


    /**
     * 获取审核中视频
     * @param count
     * @return
     */
    @Select("select * from media_files t where t.audit_status = '002003' and file_type = '001003' limit #{count}")
    List<MediaFiles> getMediaToBeModerationList(int count);

}
