package com.dico.modules.controller;

import com.dico.feign.domain.SysMenu;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.SmsRegions;
import com.dico.modules.service.SmsRegionsService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

/**
 * 区域模块
 */
@RestController
@Api(tags = "区域模块", produces = "区域模块Api")
public class SmsRegionsController {

    @Autowired
    private SmsRegionsService smsRegionsService;

    @Autowired
    private DicoBaseClient dicoBaseClient;

    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsRegions/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false) String pid, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsRegions> smsRegionsPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            if (StringUtils.isNotBlank(pid)) {
                queryMap.put("pid_EQ", pid);
            }
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsRegionsPage = smsRegionsService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsRegionsPage);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsRegions/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request, @RequestParam(required = false) String pid, @RequestParam(required = false) String name) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            if (StringUtils.isNotBlank(pid)) {
                queryMap.put("pid_EQ", pid);
            }
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = smsRegionsService.findAll(queryMap, sort, request);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 获取树形数据
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/smsRegions/treeData")
    @ApiOperation(value = "获取树形数据", notes = "获取树形数据")
    public ResultData treeData() {
        return ResultData.getSuccessResult(smsRegionsService.findTreeData());
    }

    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/smsRegions/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsRegionsId) {
        SmsRegions smsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(smsRegionsId);
        if (null == smsRegions) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsRegions);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/smsRegions/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsRegions smsRegions) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsRegions);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            if(smsRegions.getName()==""){
                return ResultData.getFailResult("区域名称不许为空");
            }
            List<SmsRegions> getSmsRegionsList= smsRegionsService.getSmsRegionsByNameAndDelFlagIsFalse(smsRegions.getName());
            if(getSmsRegionsList.size()!=0){
                return ResultData.getFailResult("区域已存在，区域名称重复");
            }
            SmsRegions parentSmsRegions = null;
            if (StringUtils.isNotBlank(smsRegions.getPid())) {
                parentSmsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(smsRegions.getPid());
                if(parentSmsRegions==null) {
                    return ResultData.getFailResult("上级区域不存在");
                }
            }
            smsRegions.setCreateUser(userId);
            smsRegions.setCreateDate(new Date());
            smsRegions.setDelFlag(false);
            smsRegionsService.save(smsRegions);
            List<SmsRegions> smsRegionsList= smsRegionsService.getSmsRegionsByNameAndDelFlagIsFalse(smsRegions.getName());
            SysMenu sysMenu = new SysMenu();
            if(smsRegionsList.size()!=1){
                return ResultData.getFailResult("区域名称重复,菜单添加区域失败");
            }
            SmsRegions smsRegions1=smsRegionsList.get(0);
            if (smsRegions1.getPid()!=null && smsRegions.getPid()!="") {
                    SysMenu parentSyaMenu = dicoBaseClient.getSysMenuByAreaId(smsRegions1.getPid());
                    sysMenu.setLevel(String.valueOf(Integer.valueOf(parentSyaMenu.getLevel()) + 1));
                    sysMenu.setAreaId(smsRegions1.getId());
                    sysMenu.setParent(parentSyaMenu.getId());
                    sysMenu.setAddress(parentSyaMenu.getAddress());
            } else {
                SysMenu parentSyaMenu = dicoBaseClient.getSysMenuIsAreaRoot();
                sysMenu.setLevel(String.valueOf(Integer.valueOf(parentSyaMenu.getLevel()) + 1));
                sysMenu.setAreaId(smsRegions1.getId());
                sysMenu.setParent(parentSyaMenu.getId());
                sysMenu.setAddress(parentSyaMenu.getAddress());
            }
            sysMenu.setName(smsRegions.getName());
            sysMenu.setCreateDate(new Date());
            sysMenu.setCreateUser(userId);
            sysMenu.setDelFlag(false);
            dicoBaseClient.saveMenuEntity(sysMenu);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
    /**
     * 更新方法
     */
    @ResponseBody
    @Transactional
    @PutMapping("/smsRegions/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsRegions smsRegions) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsRegions _smsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(smsRegions.getId());
            if (null == _smsRegions) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsRegions);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsRegions.setUpdateUser(userId);
            _smsRegions.setUpdateDate(new Date());
            if (!smsRegions.getName().equals(_smsRegions.getName())) {
                SysMenu menuSmsRegions = dicoBaseClient.getSysMenuByAreaId(_smsRegions.getId());
                if (null != menuSmsRegions) {
                    menuSmsRegions.setName(smsRegions.getName());
                }
                menuSmsRegions.setUpdateDate(new Date());
                menuSmsRegions.setUpdateUser(userId);
                dicoBaseClient.generatorAreaMenus(menuSmsRegions);
            }
            if (StringUtils.isNotBlank(smsRegions.getPid()) && !smsRegions.getPid().equals(_smsRegions.getPid())) {
                SysMenu parentSysMenu = dicoBaseClient.getSysMenuByAreaId(smsRegions.getPid());
                SysMenu sysMenu = dicoBaseClient.getSysMenuByAreaId(smsRegions.getId());
                sysMenu.setLevel(String.valueOf(Integer.valueOf(parentSysMenu.getLevel()) + 1));
                sysMenu.setParent(parentSysMenu.getId());
                sysMenu.setUpdateDate(new Date());
                sysMenu.setUpdateUser(userId);
                dicoBaseClient.generatorAreaMenus(sysMenu);
            }
            TransmitUtils.sources2destination(smsRegions, _smsRegions);
            smsRegionsService.update(_smsRegions);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsRegions/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsRegionsService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsRegions/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsRegions smsRegions = smsRegionsService.findOne(id);
                if (null != smsRegions) {
                    // 设置删除标识为真
                    smsRegions.setDelFlag(true);
                    smsRegions.setUpdateDate(new Date());
                    smsRegions.setUpdateUser(userId);
                    smsRegionsService.update(smsRegions);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}
