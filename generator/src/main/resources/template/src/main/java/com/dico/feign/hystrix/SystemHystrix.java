package com.dico.feign.hystrix;

import com.dico.feign.domain.SysRole;
import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.SystemFeignClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户熔断处理类
 *
 * @author Gaodl
 * 方法名称:
 * 参数：
 * 返回值：
 * 创建时间: 2018/12/28
 */
@Component
// spring动态代理设置为cglib代理才能够使用熔断机制
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SystemHystrix implements SystemFeignClient {

    @Override
    public List<SysRole> getBindRoles(String userId) {
        return null;
    }

    @Override
    public boolean isSuper(String userId) {
        return false;
    }

    @Override
    public SysUser findOne(String userId) {
        return null;
    }
}
