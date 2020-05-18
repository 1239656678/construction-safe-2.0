package com.dico.modules.controller;

import com.dico.modules.domain.CurrentSysMenu;
import com.dico.modules.domain.SysMenu;
import com.dico.modules.domain.SysUser;
import com.dico.modules.service.SysMenuService;
import com.dico.modules.service.SysUserService;
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
 * 菜单业务模块
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysMenuController
 * 创建时间: 2018/12/25
 */
@RestController
@Api(tags = "菜单模块", produces = "菜单模块Api")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询方法
     *
     * @author Gaodl
     * 方法名称: findList
     * 参数： [name, parent, pageNum, pageSize]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @GetMapping("/menu/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据方法")
    public ResultData dataPage(String name, String parent, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SysMenu> sysMenuPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            if (StringUtils.isNotBlank(parent)) {
                queryMap.put("parent_EQ", parent);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            sysMenuPage = sysMenuService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(sysMenuPage);
    }

    /**
     * 分页查询方法
     *
     * @author Gaodl
     * 方法名称: findList
     * 参数： [name, parent, pageNum, pageSize]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @GetMapping("/menu/findCurrentMenu")
    @ApiOperation(value = "获取当前用户所有菜单", notes = "获取当前用户所有菜单")
    public ResultData findCurrentMenu(HttpServletRequest request) {
        String userId = TokenUtils.getUserIdByRequest(request);
        SysUser currentUser = sysUserService.getUserByIdAndDelFlagIsFalse(userId);
        if (null == currentUser) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        List<CurrentSysMenu> currentMenuList = sysMenuService.findAll(currentUser);
        return ResultData.getSuccessResult(currentMenuList);
    }

    /**
     * 不分页获取数据
     *
     * @author Gaodl
     * 方法名称: dataList
     * 参数： [name, parent]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @GetMapping("/menu/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(String name, String parent) {
        List<SysMenu> sysMenuList;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            if (StringUtils.isNotBlank(parent)) {
                queryMap.put("parent_EQ", parent);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            sysMenuList = sysMenuService.findAll(queryMap, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(sysMenuList);
    }

    /**
     * 新增方法
     *
     * @author Gaodl
     * 方法名称: save
     * 参数： [request, sysMenu]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/25
     */
    @ResponseBody
    @PostMapping("/menu/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SysMenu sysMenu) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(sysMenu);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            sysMenu.setCreateDate(new Date());
            sysMenu.setCreateUser(userId);
            sysMenuService.save(sysMenu);
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
    @PutMapping("/menu/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SysMenu sysMenu) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SysMenu _sysMenu = sysMenuService.getByIdAndDelFlagIsFalse(sysMenu.getId());
            if (null == _sysMenu) {
                return ResultData.getFailResult("该菜单不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(sysMenu);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            TransmitUtils.sources2destination(sysMenu, _sysMenu);
            _sysMenu.setUpdateDate(new Date());
            _sysMenu.setUpdateUser(userId);
            sysMenuService.update(_sysMenu);
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
    @DeleteMapping("/menu/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        sysMenuService.deleteByIdIn(ids.split(","));
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
    @DeleteMapping("/menu/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SysMenu sysMenu = sysMenuService.findOne(id);
                if (null != sysMenu) {
                    sysMenu.setDelFlag(true);// 设置删除标识为真
                    sysMenu.setUpdateUser(userId);
                    sysMenuService.update(sysMenu);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}
