package com.dico.common.config;

import com.dico.common.interceptor.FeignInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 设置request可传递
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: feignSupportHeader
 * 创建时间: 2019/1/9
 */
@Configuration
public class FeignSupportHeader {
    @Bean
    public RequestInterceptor getRequestInterceptor() {
        return new FeignInterceptor();
    }
}
