package com.dico.modules.controller;

import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.SmsEquipment;
import com.dico.modules.domain.SmsEquipmentClass;
import com.dico.modules.domain.SmsEquipmentClassTarget;
import com.dico.modules.domain.SmsInspectionTarget;
import com.dico.modules.service.SmsEquipmentClassService;
import com.dico.modules.service.SmsEquipmentClassTargetService;
import com.dico.modules.service.SmsEquipmentService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 设备类型模块
 */
@RestController
@Api(tags = "设备类型模块", produces = "设备类型模块Api")
public class SmsEquipmentClassController {

    @Autowired
    private SmsEquipmentClassService smsEquipmentClassService;

    @Autowired
    private SmsEquipmentClassTargetService smsEquipmentClassTargetService;

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    /**
     * 分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataPage
     * 参数： [request, className, pageNum, pageSize]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/10
     */
    @ResponseBody
    @GetMapping("/smsEquipmentClass/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(HttpServletRequest request, @RequestParam(required = false, defaultValue = "", value = "className") String className, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsEquipmentClass> smsEquipmentClassPage;
        String userId = TokenUtils.getUserIdByRequest(request);
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            queryMap.put("className_LIKE", className);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsEquipmentClassPage = smsEquipmentClassService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsEquipmentClassPage);
    }

    /**
     * 不分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataList
     * 参数： [request, className]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/10
     */
    @ResponseBody
    @GetMapping("/smsEquipmentClass/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request, @RequestParam(required = false, defaultValue = "", value = "className") String className, @RequestParam(required = false, defaultValue = "", value = "classCode") String classCode) {
        List<Map<String, Object>> listDataMap;
        String userId = TokenUtils.getUserIdByRequest(request);
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(className)) {
                queryMap.put("className_LIKE", className);
            }
            if (StringUtils.isNotBlank(classCode)) {
                queryMap.put("classCode_LIKE", classCode);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = smsEquipmentClassService.findAll(queryMap, sort, request);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 获取数据详情
     *
     * @author Gaodl
     * 方法名称: dataInfo
     * 参数： [smsEquipmentClassId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/10
     */
    @ResponseBody
    @GetMapping("/smsEquipmentClass/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsEquipmentClassId) {
        SmsEquipmentClass smsEquipmentClass = smsEquipmentClassService.getSmsEquipmentClassByIdAndDelFlagIsFalse(smsEquipmentClassId);
        if (null == smsEquipmentClass) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsEquipmentClass);
    }

    /**
     * 新增方法
     *
     * @author Gaodl
     * 方法名称: save
     * 参数： [request, smsEquipmentClass]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/10
     */
    @ResponseBody
    @PostMapping("/smsEquipmentClass/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsEquipmentClass smsEquipmentClass) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEquipmentClass);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            // 查询设备类型名称是否存在
            SmsEquipmentClass _smsEquipmentClass = smsEquipmentClassService.findByClassNameEqualsAndParentClassEqualsAndDelFlagIsFalse(smsEquipmentClass.getClassName(), smsEquipmentClass.getParentClass());
            if (null != _smsEquipmentClass) {
                return ResultData.getFailResult("设备类型名称已经存在");
            }
            smsEquipmentClass.setCreateUser(userId);
            smsEquipmentClass.setCreateDate(new Date());
            smsEquipmentClass.setDelFlag(false);
            smsEquipmentClassService.save(smsEquipmentClass);
//            // 获取需要关联的设备ID
//            String[] checkItemIds = smsEquipmentClass.getCheckItemIds().split(",");
//            if (checkItemIds.length == 0) {
//                return ResultData.getFailResult("请选择需要绑定的检查项");
//            } else {
//                //绑定检查项,checkItemIds
//                this.bindTargets(request, smsEquipmentClass.getId(), checkItemIds);
//            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    private ResultData bindTargets(HttpServletRequest request, String equipmentClassId, String[] targetIds) {
        try {
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验信息
            if (!StringUtils.isNotBlank(equipmentClassId)) {
                return ResultData.getFailResult("设备类型不能为空");
            }
            if (targetIds.length == 0) {
                return ResultData.getFailResult("请选择需要关联的巡检项");
            }
            // 开始绑定巡检项
            List<SmsEquipmentClassTarget> smsEquipmentClassTargetList = new ArrayList<>();
            for (int i = 0; i < targetIds.length; i++) {
                // 查询该巡检项是否已经绑定，如果已经绑定则不再次绑定
                SmsEquipmentClassTarget smsEquipmentClassTarget = smsEquipmentClassTargetService.findByEquipmentClassIdEqualsAndTargetIdEqualsAndDelFlagIsFalse(equipmentClassId, targetIds[i]);
                if (null == smsEquipmentClassTarget) {
                    SmsEquipmentClassTarget newSmsEquipmentClassTarget = new SmsEquipmentClassTarget();
                    newSmsEquipmentClassTarget.setEquipmentClassId(equipmentClassId);
                    newSmsEquipmentClassTarget.setTargetId(targetIds[i]);
                    newSmsEquipmentClassTarget.setCreateDate(new Date());
                    newSmsEquipmentClassTarget.setCreateUser(userId);
                    newSmsEquipmentClassTarget.setDelFlag(false);
                    smsEquipmentClassTargetList.add(newSmsEquipmentClassTarget);
                }
            }
            smsEquipmentClassTargetService.save(smsEquipmentClassTargetList);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     *
     * @author Gaodl
     * 方法名称: update
     * 参数： [request, smsEquipmentClass]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/10
     */
    @ResponseBody
    @PutMapping("/smsEquipmentClass/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsEquipmentClass smsEquipmentClass) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsEquipmentClass _smsEquipmentClass = smsEquipmentClassService.getSmsEquipmentClassByIdAndDelFlagIsFalse(smsEquipmentClass.getId());
            if (null == _smsEquipmentClass) {
                return ResultData.getFailResult("该信息不存在");
            }

            // 查询设备类型名称是否存在
            SmsEquipmentClass _smsEquipmentClass1 = smsEquipmentClassService.findByClassNameEqualsAndParentClassEqualsAndDelFlagIsFalse(smsEquipmentClass.getClassName(), smsEquipmentClass.getParentClass());
            if (null != _smsEquipmentClass1 && !_smsEquipmentClass1.getId().equals(_smsEquipmentClass.getId())) {
                return ResultData.getFailResult("设备类型名称已经存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEquipmentClass);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            TransmitUtils.sources2destination(smsEquipmentClass, _smsEquipmentClass);
            _smsEquipmentClass.setUpdateUser(userId);
            _smsEquipmentClass.setUpdateDate(new Date());
            smsEquipmentClassService.update(_smsEquipmentClass);
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
     * 创建时间: 2019/4/10
     */
    @ResponseBody
    @DeleteMapping("/smsEquipmentClass/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsEquipmentClassService.deleteByIdIn(ids.split(","));
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
     * 创建时间: 2019/4/10
     */
    @ResponseBody
    @DeleteMapping("/smsEquipmentClass/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            List<SmsEquipmentClass> smsEquipmentClassList = new ArrayList<>();
            for (String id : idStr) {
                // 根据ID查询数据
                SmsEquipmentClass smsEquipmentClass = smsEquipmentClassService.findOne(id);
                List<SmsEquipmentClass> childrens = smsEquipmentClassService.findByParentClass(id);
                if(null != childrens && childrens.size() > 0){
                    return ResultData.getFailResult(smsEquipmentClass.getClassName()+"下有有未删除的设备分类");
                }
                if (null != smsEquipmentClass) {
                    // 查询设备分类下是否有未删除的设备
                    List<SmsEquipment> smsEquipmentList = smsEquipmentService.findByClassId(smsEquipmentClass.getId());
                    if(null == smsEquipmentList || smsEquipmentList.size() == 0){
                        // 设置删除标识为真
                        smsEquipmentClass.setDelFlag(true);
                        smsEquipmentClass.setUpdateUser(userId);
                        smsEquipmentClass.setUpdateDate(new Date());
                        smsEquipmentClassList.add(smsEquipmentClass);
                    }else{
                        return ResultData.getFailResult(smsEquipmentClass.getClassName()+"下有关联的设备");
                    }
                }
            }
            smsEquipmentClassService.save(smsEquipmentClassList);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 设备类型绑定检查项
     *
     * @author Gaodl
     * 方法名称: bindTargets
     * 参数： [equipmentClassId, targetIds]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/19
     */
    @ResponseBody
    @PostMapping("/smsEquipmentClass/bindTargets")
    @ApiOperation(value = "设备类型绑定巡检项", notes = "设备类型绑定巡检项")
    public ResultData bindTargets(HttpServletRequest request, @RequestParam(value = "equipmentClassId") String equipmentClassId, @RequestParam(value = "targetIds") String targetIds) {
        String[] targetIdArray = null;
        if (StringUtils.isNotBlank(targetIds)) {
            targetIdArray = targetIds.split(",");
        }
        return this.bindTargets(request, equipmentClassId, targetIdArray);
    }

    /**
     * 获取设备类型绑定的检查项
     *
     * @author Gaodl
     * 方法名称: findBindTargets
     * 参数： [equipmentClassId]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/19
     */
    @ResponseBody
    @GetMapping("/smsEquipmentClass/findBindTargets")
    @ApiOperation(value = "获取设备类型绑定的巡检项", notes = "获取设备类型绑定的巡检项")
    public ResultData findBindTargets(String equipmentClassId) {
        if (StringUtils.isBlank(equipmentClassId)) {
            return ResultData.getFailResult("设备ID不能为空");
        }
        List<SmsInspectionTarget> smsInspectionTargetList = smsEquipmentClassTargetService.findBindTargets(equipmentClassId);
        return ResultData.getSuccessResult(smsInspectionTargetList);
    }

    /**
     * 删除设备类型绑定的巡检项
     *
     * @author Gaodl
     * 方法名称: removeBindTargets
     * 参数： [equipmentClassId, targetIds]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/19
     */
    @ResponseBody
    @DeleteMapping("/smsEquipmentClass/removeBindTargets")
    @ApiOperation(value = "删除设备类型绑定的巡检项", notes = "删除设备类型绑定的巡检项")
    public ResultData removeBindTargets(HttpServletRequest request, String equipmentClassId, String[] targetIds) {
        if (StringUtils.isBlank(equipmentClassId)) {
            return ResultData.getFailResult("设备类型ID不能为空");
        }
        if (targetIds.length == 0) {
            return ResultData.getFailResult("请选择需要解除绑定的巡检项");
        }
        String userId = TokenUtils.getUserIdByRequest(request);
        for (int i = 0; i < targetIds.length; i++) {
            SmsEquipmentClassTarget smsEquipmentClassTarget = smsEquipmentClassTargetService.findByEquipmentClassIdEqualsAndTargetIdEqualsAndDelFlagIsFalse(equipmentClassId, targetIds[i]);
            if (null != smsEquipmentClassTarget) {
                smsEquipmentClassTarget.setDelFlag(true);
                smsEquipmentClassTarget.setUpdateDate(new Date());
                smsEquipmentClassTarget.setUpdateUser(userId);
                smsEquipmentClassTargetService.update(smsEquipmentClassTarget);
            }
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 获取树形列表
     *
     * @author Gaodl
     * 方法名称: removeBindTargets
     * 参数： [equipmentClassId, targetIds]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/19
     */
    @ResponseBody
    @GetMapping("/smsEquipmentClass/findTreeList")
    @ApiOperation(value = "获取树形数据", notes = "获取树形数据")
    public ResultData findTreeList(HttpServletRequest request, String className) {
        List<SmsEquipmentClass> smsEquipmentClassList;
        try {
            if (StringUtils.isNotBlank(className)) {
                smsEquipmentClassList = smsEquipmentClassService.getSmsEquipmentClassByClassNameLike("%" + className + "%");
                smsEquipmentClassList = this.quoteChildrenList(smsEquipmentClassList);
                return ResultData.getSuccessResult(smsEquipmentClassList);
            } else {
                smsEquipmentClassList = smsEquipmentClassService.findParentList();
                smsEquipmentClassList = this.quoteChildrenList(smsEquipmentClassList);
                return ResultData.getSuccessResult(smsEquipmentClassList);
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
    }

    /**
     * 递归获取设备分类所有子节点
     *
     * @author Gaodl
     * 方法名称: quoteChildrenList
     * 参数： [smsEquipmentClassList]
     * 返回值： java.util.List<com.dico.modules.domain.SmsEquipmentClass>
     * 创建时间: 2019/4/22
     */
    private List<SmsEquipmentClass> quoteChildrenList(List<SmsEquipmentClass> smsEquipmentClassList) {
        smsEquipmentClassList.forEach(smsEquipmentClass -> {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("parentClass_EQ", smsEquipmentClass.getId());
            queryMap.put("delFlag_EQ", false);
            List<SmsEquipmentClass> childrenClassList = smsEquipmentClassService.findAll(queryMap);
            if (null != childrenClassList && childrenClassList.size() > 0) {
                this.quoteChildrenList(childrenClassList);
                smsEquipmentClass.setChildren(childrenClassList);
            }
        });
        return smsEquipmentClassList;
    }
}
