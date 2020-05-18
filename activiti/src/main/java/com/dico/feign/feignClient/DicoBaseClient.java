package com.dico.feign.feignClient;

import com.dico.feign.domain.SysMenu;
import com.dico.feign.domain.SysRole;
import com.dico.feign.domain.SysUser;
import com.dico.feign.hystrix.DicoBaseHystrix;
import com.dico.result.ImageResult;
import com.dico.result.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
@FeignClient(value = "dico-base", path = "dicoBaseServer", fallbackFactory = DicoBaseHystrix.class)
public interface DicoBaseClient {

    @GetMapping(value = "/getBindRoles")
    List<SysRole> getBindRoles(@RequestParam(name = "userId") String userId);

    @GetMapping(value = "/isSuper")
    boolean isSuper(@RequestParam(name = "userId") String userId);

    @GetMapping(value = "/findUserById")
    SysUser findUserById(@RequestParam(name = "userId") String userId);

    @PostMapping(value = "/generatorAreaMenus")
    ResultData generatorAreaMenus(@RequestBody SysMenu sysMenu);

    @GetMapping("/getSysMenuByAreaId")
    SysMenu getSysMenuByAreaId(@RequestParam(name = "areaId") String areaId);

    @GetMapping(value = "/findAttachmentListByTargetId")
    ResultData findAttachmentListByTargetId(@RequestParam(value = "targetId") String targetId);

    @GetMapping("findOrganizationById")
    ResultData findOrganizationById(@RequestParam(value = "organizationId") String organizationId);

    @GetMapping("/getSysMenuIsAreaRoot")
    SysMenu getSysMenuIsAreaRoot();

    @PostMapping(value = "/generatorTextQrCode")
    ImageResult generatorTextQrCode(@RequestParam(defaultValue = "", value = "content") String content, @RequestParam(defaultValue = "", value = "viewMsg") String viewMsg, @RequestParam(defaultValue = "800", value = "width", required = false) int width, @RequestParam(defaultValue = "800", value = "height", required = false) int height);

    @GetMapping("/findSysMenuByCurrentUser")
    ResultData findSysMenuByCurrentUser();
}
