package com.dico.common.filter;

import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: DeviceResolverRequestFilter
 * 创建时间: $date$
 */
@Component
public class DeviceResolverRequestFilter extends OncePerRequestFilter {
    private final DeviceResolver deviceResolver;

    @Autowired
    private DicoBaseClient dicoBaseClient;

    /**
     * Create a device resolving {@link Filter} that defaults to a {@link LiteDeviceResolver} implementation.
     */
    public DeviceResolverRequestFilter() {
        this(new LiteDeviceResolver());
    }

    /**
     * Create a device resolving {@link Filter}.
     *
     * @param deviceResolver the device resolver to delegate to.
     */
    public DeviceResolverRequestFilter(DeviceResolver deviceResolver) {
        this.deviceResolver = deviceResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Device device = deviceResolver.resolveDevice(request);
        request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);

        /**
         * 设置请求数据（是否系统管理员）
         */
        String userId = TokenUtils.getUserIdByRequest(request);
        boolean isSuper = dicoBaseClient.isSuper(userId);
        request.setAttribute("isSuper", isSuper);
        filterChain.doFilter(request, response);
    }
}
