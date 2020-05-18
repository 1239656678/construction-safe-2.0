package com.dico.common.interceptor;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 设置request可传递
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: feignInterceptor
 * 创建时间: 2019/1/9
 */
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, String> headers = getHeaders();
        for (String headerName : headers.keySet()) {
            requestTemplate.header(headerName, getHeaders().get(headerName));
        }
    }

    /**
     * 返回所有header 中的数据
     *
     * @author Gaodl
     * 方法名称: getHeaders
     * 参数： []
     * 返回值： java.util.Map<java.lang.String,java.lang.String>
     * 创建时间: 2019/1/8
     */
    private Map<String, String> getHeaders() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}