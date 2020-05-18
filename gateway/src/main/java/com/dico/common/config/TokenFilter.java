package com.dico.common.config;

import com.dico.utils.TokenHelper;
import com.dico.utils.WriteUtils;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * Token过滤器
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: TokenFilter
 * 创建时间: 2018/12/7
 */
@Slf4j
@Component
public class TokenFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret}")
    public String SECRET;

    @Value("${jwt.header}")
    private String AUTH_HEADER;

    @Bean
    private TokenHelper tokenHelper() {
        return new TokenHelper(SECRET, AUTH_HEADER);
    }

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
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String requestUrl = serverHttpRequest.getURI().getPath();
        // 如果请求的是登录方法， 则不校验token
        if ("/dico-base/auth/login".equals(requestUrl)) {
            return chain.filter(exchange);
        }
        String AuthorizationToken = serverHttpRequest.getHeaders().getFirst(AUTH_HEADER);
        // 如果token为空则返回错误信息
        if (StringUtils.isNotBlank(AuthorizationToken) && AuthorizationToken.startsWith("Bearer ")) {
            AuthorizationToken = AuthorizationToken.substring(7);

        } else {
            return WriteUtils.generateMsgBack(exchange, 1, "找不到身份令牌");
        }
        // 校验token是否合法,不合法返回错误信息
        if (tokenHelper().validateToken(AuthorizationToken)) {
            return WriteUtils.generateMsgBack(exchange, 1, "鉴权失败");
        }
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
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
