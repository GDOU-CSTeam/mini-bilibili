package com.sky.common.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used to configure the pagination behavior for MyBatis.
 * It uses the MyBatisPlusInterceptor to add a PaginationInnerInterceptor.
 * This allows MyBatis to correctly handle pagination of results.
 */
@Configuration
public class PageConfig {

    /*
      This method was used in versions prior to 3.4.0 to configure pagination.
      It has been commented out as it is no longer needed.
      @return PaginationInterceptor
     */
    /* @Bean
    public PaginationInterceptor paginationInterceptor(){
        return  new PaginationInterceptor();
    }*/

    /**
     * This method is used to configure the MyBatisPlusInterceptor.
     * It adds a PaginationInnerInterceptor to the MyBatisPlusInterceptor.
     * This method is used in versions 3.4.0 and later, as the PaginationInterceptor has been deprecated.
     * @return MybatisPlusInterceptor configured with a PaginationInnerInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        final MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
