package com.dico.modules.controller;

import com.dico.modules.domain.SysRole;
import com.dico.modules.domain.SysUser;
import com.dico.modules.domain.SysUserRole;
import com.dico.modules.service.SysRoleService;
import com.dico.modules.service.SysUserRoleService;
import com.dico.modules.service.SysUserService;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.HashUtils;
import com.dico.util.TokenUtils;
import com.dico.util.TransmitUtils;
import com.dico.util.ValiDatedUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户业务模块
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: UserController
 * 创建时间: 2018/12/20
 */
@RestController
@Api(tags = "用户模块", produces = "用户模块Api")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

//    @Autowired
//    private OrganizationFeignClient organizationFeignClient;
//
//    @Autowired
//    private NoticeMessageFeignClient noticeMessageFeignClient;

    /**
     * 分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataPage
     * 参数： [username, name, pageNum, pageSize]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @GetMapping("/user/dataPage")
    @ApiOperation(value = "获取用户分页数据", notes = "获取分页数据方法")
    public ResultData dataPage(HttpServletRequest request, String username, String name, String organizationId, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SysUser> sysUserPage;

        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(username)) {
                queryMap.put("username_LIKE", username);
            }
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            if (StringUtils.isNotBlank(organizationId)) {
                queryMap.put("organizationId_EQ", organizationId);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            sysUserPage = sysUserService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(sysUserPage);
    }

    /**
     * 不分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataList
     * 参数： [username, name]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @GetMapping("/user/dataList")
    @ApiOperation(value = "获取用户数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request, String username, String name, String organizationId) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(username)) {
                queryMap.put("username_LIKE", username);
            }
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            if (StringUtils.isNotBlank(organizationId)) {
                queryMap.put("organizationId_EQ", organizationId);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = sysUserService.findAll(queryMap, sort, request);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 新增方法
     *
     * @author Gaodl
     * 方法名称: save
     * 参数： [request, sysUser]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @PostMapping("/user/save")
    @ApiOperation(value = "用户保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SysUser sysUser) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(sysUser);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            // 判断用户名是否存在
            SysUser _sysUser = sysUserService.getUserByUsername(sysUser.getUsername());
            if (null != _sysUser) {
                return ResultData.getFailResult("用户名已经存在");
            }
            // 判断用户是否为临时用户
            if (sysUser.isTemporary()) {
                if (null == sysUser.getAllowBeginLoginDate() || null == sysUser.getAllowEndLoginDate()) {
                    return ResultData.getFailResult("临时用户需要指定可登录时间");
                }
            }
            // 生成盐
            sysUser.setSalt(HashUtils.toHash(Md5Hash.ALGORITHM_NAME, sysUser.getUsername()));
            // 加密用户密码
            sysUser.setPassword(HashUtils.toComplexHashIterations(Sha1Hash.ALGORITHM_NAME, sysUser.getPassword(), sysUser.getSalt()));
            sysUser.setCreateDate(new Date());
            sysUser.setCreateUser(userId);
            sysUserService.save(sysUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     *
     * @author Gaodl
     * 方法名称: update
     * 参数： [request, sysUser]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @PutMapping("/user/update")
    @ApiOperation(value = "用户修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SysUser sysUser) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SysUser _sysUser = sysUserService.getUserByIdAndDelFlagIsFalse(sysUser.getId());
            if (null == _sysUser) {
                return ResultData.getFailResult("该用户不存在");
            }

            // 判断用户名是否被修改
            if (!_sysUser.getUsername().equals(sysUser.getUsername())) {
                return ResultData.getFailResult("不能修改用户名");
            }
            // 判断用户是否为临时用户
            if (sysUser.isTemporary()) {
                if (null == sysUser.getAllowBeginLoginDate() || null == sysUser.getAllowEndLoginDate()) {
                    return ResultData.getFailResult("临时用户需要指定可登录时间");
                }
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(sysUser);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            String oldPass = _sysUser.getPassword();
            String newPass = sysUser.getPassword();
            String newPassword = HashUtils.toComplexHashIterations(Sha1Hash.ALGORITHM_NAME, newPass, _sysUser.getSalt());
            TransmitUtils.sources2destination(sysUser, _sysUser);
            // 判断用户是否对密码做了修改, 如果做了修改, 则需要重新加密
            if (!newPass.equals(oldPass) && !newPassword.equals(oldPass)) {
                _sysUser.setPassword(newPassword);
            }
            _sysUser.setUpdateDate(new Date());
            _sysUser.setUpdateUser(userId);
            sysUserService.update(_sysUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     *
     * @author Gaodl
     * 方法名称: delete
     * 参数： [ids]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @DeleteMapping("/user/delete")
    @ApiOperation(value = "用户物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            sysUserService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     *
     * @author Gaodl
     * 方法名称: remove
     * 参数： [request, ids]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @DeleteMapping("/user/remove")
    @ApiOperation(value = "用户逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SysUser sysUser = sysUserService.findOne(id);
                if (null != sysUser) {
                    sysUser.setDelFlag(true);// 设置删除标识为真
                    sysUser.setUpdateUser(userId);
                    sysUserService.update(sysUser);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 获取当前系统登录用户
     *
     * @author Gaodl
     * 方法名称: getLoginUser
     * 参数： [request]
     * 返回值： com.dico.modules.domain.SysUser
     * 创建时间: 2018/12/28
     */
    @GetMapping("/user/getLoginUser")
    @ApiOperation(value = "获取当前系统登录用户", notes = "获取当前系统登录用户")
    public ResultData getLoginUser(HttpServletRequest request) {
        SysUser sysUser = sysUserService.getUserByIdAndDelFlagIsFalse(TokenUtils.getUserIdByRequest(request));
        return new ResultData().setCode(0).setMsg("成功").setData(sysUser);
    }

    /**
     * 查询用户所在组织机构信息
     * @author Gaodl
     * 方法名称: getOrganizationInfo
     * 参数： [userId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/3/22
     */
//    @GetMapping("/user/getOrganizationInfo")
//    @ApiOperation(value = "获取用户所在组织机构信息", notes = "获取用户所在组织机构信息")
//    public ResultData getOrganizationInfo(String userId){
//        return organizationFeignClient.findCompanyByUserId(userId);
//    }

    /**
     * 用户绑定角色
     *
     * @author Gaodl
     * 方法名称: bindRoles
     * 参数： [request, userId, roleIds]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    @PostMapping("/user/bindRoles")
    @ApiOperation(value = "用户绑定角色", notes = "给用户绑定角色")
    public ResultData bindRoles(HttpServletRequest request, String userId, String roleIds) {
        if (StringUtils.isBlank(userId)) {
            return new ResultData().setCode(1).setMsg("用户ID不能为空");
        }
        if (StringUtils.isBlank(roleIds)) {
            return new ResultData().setCode(1).setMsg("角色ID不能为空");
        }
        // 查询用户是否存在
        SysUser sysUser = sysUserService.getUserByIdAndDelFlagIsFalse(userId);
        if (null == sysUser) {
            return new ResultData().setCode(1).setMsg("用户已经被删除");
        }
        ResultData resultData = this.removeUserRoles(userId);
        if (resultData.getCode() == 1) {
            return resultData;
        }
        return this.saveUserRoles(request, userId, roleIds);
    }

    /**
     * 保存绑定角色
     *
     * @author Gaodl
     * 方法名称: bindRoles
     * 参数： [request, userId, roleIds]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    private ResultData saveUserRoles(HttpServletRequest request, String userId, String roleIds) {
        // 获取当前登陆人
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(userId)) {
            return new ResultData().setCode(1).setMsg("用户ID不能为空");
        }
        if (StringUtils.isBlank(roleIds)) {
            return new ResultData().setCode(1).setMsg("角色ID不能为空");
        }
        // 查询用户是否存在
        SysUser sysUser = sysUserService.getUserByIdAndDelFlagIsFalse(userId);
        if (null == sysUser) {
            return new ResultData().setCode(1).setMsg("该用户已经被删除");
        }
        String[] roleIdStr = roleIds.split(",");
        List<SysUserRole> addRoleList = new ArrayList<>(roleIdStr.length);
        for (int i = 0; i < roleIdStr.length; i++) {
            String roleId = roleIdStr[i];
            SysRole sysRole = sysRoleService.getByIdAndDelFlagIsFalse(roleId);
            if (null == sysRole) {
                return new ResultData().setCode(1).setMsg("选中的角色中有已经删除的角色,请刷新页面重试");
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            sysUserRole.setCreateDate(new Date());
            sysUserRole.setCreateUser(currentUserId);
            sysUserRole.setDelFlag(false);
            addRoleList.add(sysUserRole);
        }
        try {
            sysUserRoleService.save(addRoleList);
        } catch (Exception e) {
            return new ResultData().setCode(1).setMsg(e.getMessage());
        }
        return new ResultData().setCode(0).setMsg("保存成功");
    }

    /**
     * 逻辑删除用户所有角色关联信息
     *
     * @author Gaodl
     * 方法名称: deleteRoles
     * 参数： [userId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    private ResultData removeUserRoles(String userId) {
        try {
            // 根据用户ID查询用户关联角色信息
            List<SysUserRole> sysUserRoleList = sysUserRoleService.getByUserIdAndDelFlagIsFalse(userId);
            if (null != sysUserRoleList && sysUserRoleList.size() > 0) {
                for (SysUserRole sysUserRole : sysUserRoleList) {
                    sysUserRole.setDelFlag(true);
                    sysUserRoleService.update(sysUserRole);
                }
            }
        } catch (Exception e) {
            return new ResultData().setCode(1).setMsg(e.getMessage());
        }
        return new ResultData().setCode(0).setMsg("删除成功");
    }

    /**
     * 逻辑删除指定用户角色关联信息
     *
     * @author Gaodl
     * 方法名称: removeRoles
     * 参数： [userId, roleId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    @DeleteMapping("/user/removeUserRole")
    @ApiOperation(value = "逻辑删除指定用户角色关联信息", notes = "逻辑删除指定用户角色关联信息")
    public ResultData removeUserRole(String userId, String roleId) {
        if (StringUtils.isBlank(userId)) {

            return new ResultData().setCode(1).setMsg("用户ID不能为空");
        }
        if (StringUtils.isBlank(roleId)) {
            return new ResultData().setCode(1).setMsg("角色ID不能为空");
        }
        SysUserRole sysUserRole = sysUserRoleService.getByUserIdAndRoleId(userId, roleId);
        if (null == sysUserRole) {
            return new ResultData().setCode(1).setMsg("角色关联信息不存在");
        }
        sysUserRole.setDelFlag(true);
        sysUserRoleService.update(sysUserRole);
        return new ResultData().setCode(0).setMsg("删除成功");
    }

    /**
     * 物理删除指定用户角色关联信息
     *
     * @author Gaodl
     * 方法名称: deleteRole
     * 参数： [userId, roleId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    @DeleteMapping("/user/deleteUserRole")
    @ApiOperation(value = "物理删除指定用户角色关联信息", notes = "物理删除指定用户角色关联信息")
    public ResultData deleteUserRole(String userId, String roleId) {
        if (StringUtils.isBlank(userId)) {
            return new ResultData().setCode(1).setMsg("用户ID不能为空");
        }
        if (StringUtils.isBlank(roleId)) {
            return new ResultData().setCode(1).setMsg("角色ID不能为空");
        }
        sysUserRoleService.deleteByUserIdAndRoleId(userId, roleId);
        return new ResultData().setCode(0).setMsg("删除成功");
    }

    /**
     * 获取用户绑定的角色
     *
     * @author Gaodl
     * 方法名称: getBindRoles
     * 参数： []
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    @GetMapping("/user/getBindRoles")
    @ApiOperation(value = "获取用户绑定的角色", notes = "获取数据方法")
    public ResultData getBindRoles(String userId) {
        if (StringUtils.isBlank(userId)) {
            return new ResultData().setCode(1).setMsg("用户ID不能为空");
        }
        List<SysRole> sysRoleList = sysUserRoleService.getRoleByUserId(userId);
        return new ResultData().setCode(0).setMsg("查询成功").setData(sysRoleList);
    }

    /**
     * 根据ID查询用户信息
     *
     * @author xipeifeng
     * 方法名称: getLoginUser
     * 参数： [request]
     * 返回值： com.dico.modules.domain.SysUser
     * 创建时间: 2018/12/28
     */
    @GetMapping("/user/get")
    @ApiOperation(value = "根据ID获取用户", notes = "根据ID获取用户")
    public ResultData getUser(String id) {
        SysUser sysUser = sysUserService.findOne(id);
        return new ResultData().setCode(0).setMsg("成功").setData(sysUser);
    }
}

