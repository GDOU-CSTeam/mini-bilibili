package com.bili.web.mapper;

import com.bili.pojo.entity.BSeckillVoucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 秒杀优惠券表，与优惠券是一对一关系 Mapper 接口
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:14
 */
@Mapper
public interface BSeckillVoucherMapper extends BaseMapper<BSeckillVoucher> {

}
