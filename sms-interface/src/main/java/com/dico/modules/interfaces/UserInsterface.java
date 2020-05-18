package com.dico.modules.interfaces;

import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.result.ResultData;
import com.dico.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
@Slf4j
@RestController
@RequestMapping("todos")
@Api(tags = "手机端用户模块", produces = "手机端用户模块Api")
public class UserInsterface {

    @Resource
    private DicoBaseClient dicoBaseClient;

    /**
     * 修改密码
     *
     * @author Gaodl
     * 方法名称: findToDoList
     * 参数： [request, oldPassWord, newPassWord]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/5/17
     */
    @PostMapping("/updatePass")
    @ApiOperation(value = "修改密码", notes = "修改密码接口")
    public ResultData updatePass(HttpServletRequest request, @RequestParam(name = "oldPassword") String oldPassword, @RequestParam(name = "newPassword") String newPassword) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(userId)) {
            log.error("Request error with TODosInterface.findToDoList(), Cannot get the current user by [" + userId + "]");
            return ResultData.getFailResult("获取当前用户出错");
        }
        return dicoBaseClient.updatePass(userId, oldPassword, newPassword);
    }

    /**
     * 获取当前用户Id
     *
     * @author Gaodl
     * 方法名称: getCurrentUserId
     * 参数： [request]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/5/17
     */
    @GetMapping("/getCurrentUserId")
    @ApiOperation(value = "获取当前用户Id", notes = "获取当前用户Id")
    public ResultData getCurrentUserId(HttpServletRequest request) {
        try {
            String userId = TokenUtils.getUserIdByRequest(request);
            if (StringUtils.isBlank(userId)) {
                log.error("Request error with TODosInterface.findToDoList(), Cannot get the current user by [" + userId + "]");
                return ResultData.getFailResult("获取当前用户出错");
            }
            SysUser sysUser = dicoBaseClient.findUserById(userId);
            if (null == sysUser) {
                return ResultData.getFailResult("用户不存在");
            }
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("id", sysUser.getId());
            dataMap.put("organizationId", sysUser.getOrganizationId());
            return ResultData.getSuccessResult(dataMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultData.getFailResult(e.getMessage());
        }
    }

    /**
     * 判断当前用户是否为部门领导
     *
     * @author Gaodl
     * 方法名称: isOrganizationUser
     * 参数： [request]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/5/17
     */
    @GetMapping("/isOrganizationUser")
    @ApiOperation(value = "判断当前用户是否为部门领导", notes = "判断当前用户是否为部门领导")
    public ResultData isOrganizationUser(HttpServletRequest request) {
        try {
            return ResultData.getSuccessResult(dicoBaseClient.isOrganizationUser());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultData.getFailResult(e.getMessage());
        }
    }
}
