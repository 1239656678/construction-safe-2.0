package com.dico.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: MyGlobalFilter
 * 创建时间: 2018/12/7
 */
@Slf4j
@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {

    @Value("${jwt.header}")
    private String AUTH_HEADER;

    /**
     * 业务处理模块
     *
     * @author Gaodl
     * 方法名称: filter
     * 参数： [exchange, chain]
     * 返回值： reactor.core.publisher.Mono<java.lang.Void>
     * 创建时间: 2018/12/7
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("----------------------------------->进入全局过滤器");
        log.info("访问路径----------------------------------->" + exchange.getRequest().getPath());
        return chain.filter(exchange);
    }

    /**
     * 设置优先级
     *
     * @author Gaodl
     * 方法名称: getOrder
     * 参数： []
     * 返回值： int
     * 创建时间: 2018/12/7
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
