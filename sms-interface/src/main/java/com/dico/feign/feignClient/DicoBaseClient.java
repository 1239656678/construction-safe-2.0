package com.dico.feign.feignClient;

import com.dico.feign.domain.Attachments;
import com.dico.feign.domain.SysRole;
import com.dico.feign.domain.SysUser;
import com.dico.feign.domain.SysUserMap;
import com.dico.feign.hystrix.DicoBaseHystrix;
import com.dico.result.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "/findUserByOrganizationId")
    List<SysUserMap> findUserByOrganizationId(@RequestParam(name = "organizationId") String organizationId);

    @GetMapping(value = "/findAttachmentListByIds")
    List<Attachments> findAttachmentListByIds(@RequestParam(name = "ids") String[] ids);

    @GetMapping(value = "/findAttachmentListByTargetId")
    ResultData findAttachmentListByTargetId(@RequestParam(value = "targetId") String targetId);

    @PutMapping("/updateAttchment")
    ResultData updateAttchment(@RequestBody Attachments attachments);

    @PostMapping(value = "/updatePass")
    ResultData updatePass(@RequestParam(name = "userId") String userId, @RequestParam(name = "oldPassWord") String oldPassWord, @RequestParam(name = "newPassWord") String newPassWord);

    @GetMapping(value = "/isOrganizationUser")
    Boolean isOrganizationUser();

    @GetMapping(value = "/isWxxz")
    Boolean isWxxz();

    @GetMapping(value = "/isWbdw")
    Boolean isWbdw();

    /**
     * 根据ID查询附件
     *
     * @param id
     * @return
     */
     @GetMapping("/getAttachment")
     Attachments getAttachment(@RequestParam(value = "id") String id);
}
