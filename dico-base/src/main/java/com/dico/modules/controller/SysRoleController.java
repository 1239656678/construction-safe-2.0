package com.dico.modules.controller;

import com.dico.modules.domain.SysMenu;
import com.dico.modules.domain.SysRole;
import com.dico.modules.domain.SysRoleMenu;
import com.dico.modules.service.SysMenuService;
import com.dico.modules.service.SysRoleMenuService;
import com.dico.modules.service.SysRoleService;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.TokenUtils;
import com.dico.util.TransmitUtils;
import com.dico.util.ValiDatedUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 角色业务模块
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysRoleController
 * 创建时间: 2018/12/24
 */
@RestController
@Api(tags = "角色模块", produces = "角色模块Api")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataPage
     * 参数： [code, name, pageNum, pageSize]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @GetMapping("/role/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据方法")
    public ResultData dataPage(String code, String name, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SysRole> sysRolePage;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(code)) {
                queryMap.put("code_LIKE", code);
            }
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            sysRolePage = sysRoleService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(sysRolePage);
    }

    /**
     * 不分页获取数据
     *
     * @author Gaodl
     * 方法名称: dataList
     * 参数： [code, name]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @GetMapping("/role/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(String code, String name) {
        List<SysRole> sysRoleList;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(code)) {
                queryMap.put("code_LIKE", code);
            }
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            sysRoleList = sysRoleService.findAll(queryMap, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(sysRoleList);
    }

    /**
     * 新增方法
     *
     * @author Gaodl
     * 方法名称: save
     * 参数： [request, sysRole]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @PostMapping("/role/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SysRole sysRole) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(sysRole);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            // 判断编码是否存在
            SysRole _sysRole = sysRoleService.getOneByCodeEqualsAndDelFlagIsFalse(sysRole.getCode());
            if (null != _sysRole) {
                return ResultData.getFailResult("角色已经存在");
            }
            sysRole.setCreateDate(new Date());
            sysRole.setCreateUser(userId);
            sysRoleService.save(sysRole);
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
     * 参数： [request, sysRole]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @PutMapping("/role/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SysRole sysRole) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SysRole _sysRole = sysRoleService.getByIdAndDelFlagIsFalse(sysRole.getId());
            if (null == _sysRole) {
                return ResultData.getFailResult("该角色不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(sysRole);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            TransmitUtils.sources2destination(sysRole, _sysRole);
            _sysRole.setUpdateDate(new Date());
            _sysRole.setUpdateUser(userId);
            sysRoleService.update(_sysRole);
        } catch (Exception e) {
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
    @DeleteMapping("/role/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        String[] idStr = ids.split(",");
        for (String id : idStr) {
            // 根据ID查询数据
            SysRole sysRole = sysRoleService.findOne(id);
            if ("super".equals(sysRole.getCode())) {
                return ResultData.getFailResult("不能删除系统管理员角色");
            }
        }
        sysRoleService.deleteByIdIn(ids.split(","));
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
    @DeleteMapping("/role/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            List<SysRole> sysRoleList = new ArrayList<>();
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SysRole sysRole = sysRoleService.findOne(id);
                if ("super".equals(sysRole.getCode())) {
                    return ResultData.getFailResult("不能删除系统管理员角色");
                }
                if (null != sysRole) {
                    sysRole.setDelFlag(true);// 设置删除标识为真
                    sysRole.setUpdateUser(userId);
                    sysRoleList.add(sysRole);
                }
            }
            for (SysRole sysRole : sysRoleList) {
                sysRoleService.update(sysRole);
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 角色绑定资源
     *
     * @author Gaodl
     * 方法名称: bindMenus
     * 参数： [request, userId, roleIds]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    @PostMapping("/role/bindMenus")
    @ApiOperation(value = "角色绑定资源", notes = "给角色绑定资源")
    public ResultData bindMenus(HttpServletRequest request, String roleId, String menuIds) {
        if (StringUtils.isBlank(roleId)) {
            return new ResultData().setCode(1).setMsg("角色ID不能为空");
        }
        if (StringUtils.isBlank(menuIds)) {
            return new ResultData().setCode(1).setMsg("资源ID不能为空");
        }
        // 查询角色是否存在
        SysRole sysRole = sysRoleService.getByIdAndDelFlagIsFalse(roleId);
        if (null == sysRole) {
            return new ResultData().setCode(1).setMsg("角色已经被删除");
        }
        ResultData resultData = this.removeRoleMenus(roleId);
        if (resultData.getCode() == 1) {
            return resultData;
        }
        return this.saveRoleMenus(request, roleId, menuIds);
    }

    /**
     * 角色绑定资源信息
     *
     * @author Gaodl
     * 方法名称: saveRoleMenus
     * 参数： [request, roleId, MenuIds]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    private ResultData saveRoleMenus(HttpServletRequest request, String roleId, String menuIds) {
        // 获取当前登陆人
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(roleId)) {
            return new ResultData().setCode(1).setMsg("角色ID不能为空");
        }
        if (StringUtils.isBlank(menuIds)) {
            return new ResultData().setCode(1).setMsg("资源ID不能为空");
        }
        // 查询角色是否存在
        SysRole sysRole = sysRoleService.getByIdAndDelFlagIsFalse(roleId);
        if (null == sysRole) {
            return new ResultData().setCode(1).setMsg("该用户已经被删除");
        }
        String[] menuIdStr = menuIds.split(",");
        List<SysRoleMenu> addRoleList = new ArrayList<>(menuIdStr.length);
        for (int i = 0; i < menuIdStr.length; i++) {
            String menuId = menuIdStr[i];
            SysMenu sysMenu = sysMenuService.getByIdAndDelFlagIsFalse(menuId);
            if (null == sysMenu) {
                return new ResultData().setCode(1).setMsg("选中的菜单中有已经删除的菜单,请刷新页面重试");
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setCreateDate(new Date());
            sysRoleMenu.setCreateUser(currentUserId);
            sysRoleMenu.setDelFlag(false);
            addRoleList.add(sysRoleMenu);
        }
        try {
            sysRoleMenuService.save(addRoleList);
        } catch (Exception e) {
            return new ResultData().setCode(1).setMsg(e.getMessage());
        }
        return new ResultData().setCode(0).setMsg("保存成功");
    }

    /**
     * 逻辑删除指定角色的所有资源关联信息
     *
     * @author Gaodl
     * 方法名称: deleteRoleMenus
     * 参数： [roleId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    private ResultData removeRoleMenus(String roleId) {
        try {
            // 根据角色ID查询角色关联资源信息
            List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.getByRoleIdAndDelFlagIsFalse(roleId);
            if (null != sysRoleMenuList && sysRoleMenuList.size() > 0) {
                for (SysRoleMenu sysRoleMenu : sysRoleMenuList) {
                    sysRoleMenu.setDelFlag(true);
                    sysRoleMenuService.update(sysRoleMenu);
                }
            }
        } catch (Exception e) {
            return new ResultData().setCode(1).setMsg(e.getMessage());
        }
        return new ResultData().setCode(0).setMsg("删除成功");
    }

    /**
     * 逻辑删除指定角色资源关联信息
     *
     * @author Gaodl
     * 方法名称: removeRole
     * 参数： [userId, roleId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    @DeleteMapping("/role/removeRoleMenu")
    @ApiOperation(value = "逻辑删除指定角色资源关联信息", notes = "逻辑删除指定角色资源关联信息")
    public ResultData removeRoleMenu(String roleId, String menuId) {
        if (StringUtils.isBlank(roleId)) {
            return new ResultData().setCode(1).setMsg("角色ID不能为空");
        }
        if (StringUtils.isBlank(menuId)) {
            return new ResultData().setCode(1).setMsg("菜单ID不能为空");
        }
        SysRoleMenu sysRoleMenu = sysRoleMenuService.getByRoleIdAndMenuIdAndDelFlagIsFalse(roleId, menuId);
        if (null == sysRoleMenu) {
            return new ResultData().setCode(1).setMsg("角色关联信息不存在");
        }
        sysRoleMenu.setDelFlag(true);
        sysRoleMenuService.update(sysRoleMenu);
        return new ResultData().setCode(0).setMsg("删除成功");
    }

    /**
     * 物理删除指定角色资源关联信息
     *
     * @author Gaodl
     * 方法名称: deleteRole
     * 参数： [userId, roleId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    @DeleteMapping("/role/deleteRoleMenu")
    @ApiOperation(value = "物理删除指定角色资源关联信息", notes = "物理删除指定角色资源关联信息")
    public ResultData deleteRoleMenu(String roleId, String menuId) {
        if (StringUtils.isBlank(roleId)) {
            return new ResultData().setCode(1).setMsg("角色ID不能为空");
        }
        if (StringUtils.isBlank(menuId)) {
            return new ResultData().setCode(1).setMsg("菜单ID不能为空");
        }
        sysRoleMenuService.deleteByRoleIdAndMenuId(roleId, menuId);
        return new ResultData().setCode(0).setMsg("删除成功");
    }

    /**
     * 获取角色绑定的资源
     *
     * @author Gaodl
     * 方法名称: getBindRoles
     * 参数： [userId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/1/7
     */
    @GetMapping("/role/getBindMenus")
    @ApiOperation(value = "获取角色绑定的资源", notes = "获取数据方法")
    public ResultData getBindMenus(String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return new ResultData().setCode(1).setMsg("用户ID不能为空");
        }
        List<SysMenu> sysMenuList = sysRoleMenuService.getMenuByRoleId(roleId);
        return new ResultData().setCode(0).setMsg("查询成功").setData(sysMenuList);
    }
}
