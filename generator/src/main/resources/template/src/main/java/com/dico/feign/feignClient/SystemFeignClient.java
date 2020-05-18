package com.dico.feign.feignClient;

import com.dico.feign.domain.SysRole;
import com.dico.feign.domain.SysUser;
import com.dico.feign.hystrix.SystemHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
@FeignClient(value = "dico-system", path = "systemFeignServer", fallback = SystemHystrix.class)
public interface SystemFeignClient {

    @GetMapping(value = "/getBindRoles")
    List<SysRole> getBindRoles(@RequestParam(name = "userId") String userId);

    @GetMapping(value = "/isSuper")
    boolean isSuper(@RequestParam(name = "userId") String userId);

    @GetMapping(value = "/findOne")
    SysUser findOne(@RequestParam(name = "userId") String userId);
}
