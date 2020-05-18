package com.dico.modules.controller;

import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.SmsEquipment;
import com.dico.modules.domain.SmsMaintenancePlan;
import com.dico.modules.domain.SmsUserMaintenancePlan;
import com.dico.modules.dto.SmsEquipmentDto;
import com.dico.modules.dto.SmsMaintenanceRecordInfoDto;
import com.dico.modules.dto.SmsUserMaintenanceListDto;
import com.dico.modules.service.SmsEquipmentService;
import com.dico.modules.service.SmsMaintenancePlanService;
import com.dico.modules.service.SmsUserMaintenancePlanService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

@RestController
@Api(tags = "保养记录模块", produces = "保养记录模块Api")
public class SmsUserMaintenancePlanController{

    @Autowired
    private SmsUserMaintenancePlanService smsUserMaintenancePlanService;

    @Autowired
    private SmsMaintenancePlanService smsMaintenancePlanService;

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    @Autowired
    private DicoBaseClient dicoBaseClient;

    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsUserMaintenancePlan/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum,@RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize){
        Page<SmsUserMaintenancePlan> smsUserMaintenancePlanPage;
        try{
            Map<String, Object> queryMap=new HashMap<>(2);
            queryMap.put("delFlag_EQ",false);
            Sort sort=new Sort(Sort.Direction.DESC,"createDate");
            smsUserMaintenancePlanPage = smsUserMaintenancePlanService.findAll(queryMap,pageNum,pageSize,sort);
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsUserMaintenancePlanPage);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsUserMaintenancePlan/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request){
        List<SmsUserMaintenancePlan> smsUserMaintenancePlanList = smsUserMaintenancePlanService.findAllList();
        List<SmsUserMaintenanceListDto> smsUserMaintenanceListDtoList=new ArrayList<>();
        if(null!=smsUserMaintenancePlanList){
            for (SmsUserMaintenancePlan smsUserMaintenancePlan:smsUserMaintenancePlanList){
                SmsUserMaintenanceListDto smsUserMaintenanceListDto=new SmsUserMaintenanceListDto();
                SmsMaintenancePlan smp = smsMaintenancePlanService.getById(smsUserMaintenancePlan.getMaintenancePlanId());
                if(smp==null){
                    return ResultData.getFailResult("保养计划不存在");
                }
                if(smsUserMaintenancePlan.getStatus()!=4){
                    smsUserMaintenancePlan.setStatus(3);
                    smsUserMaintenancePlanService.update(smsUserMaintenancePlan);
                }
                SmsEquipment smsEquipment = smsEquipmentService.getSmsEquipmentByIdAndDelFlagIsFalse(smp.getEquipmentId());
                SysUser userById = dicoBaseClient.findUserById(smp.getMaintenanceUser());
                smsUserMaintenanceListDto.setId(smsUserMaintenancePlan.getId());
                smsUserMaintenanceListDto.setCycle(smp.getCycle());
                smsUserMaintenanceListDto.setEndDate(smsUserMaintenancePlan.getEndDate());
                smsUserMaintenanceListDto.setEquipmentCode(smsEquipment.getCode());
                smsUserMaintenanceListDto.setEquipmentName(smsEquipment.getName());
                smsUserMaintenanceListDto.setInstallArea(smsEquipment.getInstallRegionsName());
                smsUserMaintenanceListDto.setMaintenanceUser(userById.getName());
                smsUserMaintenanceListDto.setMaintenanceOrganization(userById.getOrganizationName());
                smsUserMaintenanceListDto.setStatus(smsUserMaintenancePlan.getStatus());
                smsUserMaintenanceListDto.setClassName(smsEquipment.getTypeName());
                smsUserMaintenanceListDtoList.add(smsUserMaintenanceListDto);
            }
            return ResultData.getSuccessResult(smsUserMaintenanceListDtoList);
        }
        return ResultData.getSuccessResult();

    }

    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/smsUserMaintenancePlan/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsUserMaintenancePlanId){
        Map<String, Object> dataMap = new HashMap<>();
        SmsUserMaintenancePlan smsUserMaintenancePlan = smsUserMaintenancePlanService.getById(smsUserMaintenancePlanId);
        if(null==smsUserMaintenancePlan){
            return ResultData.getFailResult("数据不存在");
        }
        SmsMaintenancePlan smsMaintenancePlan = smsMaintenancePlanService.getById(smsUserMaintenancePlan.getMaintenancePlanId());
        if(null == smsMaintenancePlan){
            return ResultData.getFailResult("数据不存在");
        }
        dataMap.put("equipmentInfo", null);
        SmsEquipmentDto smsEquipmentDto = smsEquipmentService.getSmsEquipmentDtoById(smsMaintenancePlan.getEquipmentId());
        if(null != smsEquipmentDto){
            dataMap.put("equipmentInfo", smsEquipmentDto);
        }
        dataMap.put("maintenanceInfo", null);
        SmsMaintenanceRecordInfoDto smsMaintenanceRecordInfo = smsUserMaintenancePlanService.findRecordInfo(smsUserMaintenancePlan.getId());
        if(null == smsMaintenanceRecordInfo){
            return ResultData.getFailResult("保养记录不存在");
        }
        System.out.println("--------------"+smsMaintenanceRecordInfo);
        if(StringUtils.isNotBlank(smsMaintenanceRecordInfo.getSmbId())){
            ResultData resultData = dicoBaseClient.findAttachmentListByTargetId(smsMaintenanceRecordInfo.getSmbId());
            if(null != resultData && resultData.getCode() == 0){
                smsMaintenanceRecordInfo.setBeforeFiles(resultData.getData());
            }
        }
        if(StringUtils.isNotBlank(smsMaintenanceRecordInfo.getSmaId())){
            ResultData resultData = dicoBaseClient.findAttachmentListByTargetId(smsMaintenanceRecordInfo.getSmaId());
            if(null != resultData && resultData.getCode() == 0){
                smsMaintenanceRecordInfo.setAfterFiles(resultData.getData());
            }
        }
        dataMap.put("maintenanceInfo", smsMaintenanceRecordInfo);
        return ResultData.getSuccessResult(dataMap);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/smsUserMaintenancePlan/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request,@RequestBody SmsUserMaintenancePlan smsUserMaintenancePlan){
        try{
            // 获取当前登录用户
            String userId=TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult=ValiDatedUtils.valiDatedBean(smsUserMaintenancePlan);
            if(!validatedResult.isStatus()){
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsUserMaintenancePlan.setCreateUser(userId);
            smsUserMaintenancePlan.setCreateDate(new Date());
            smsUserMaintenancePlan.setDelFlag(false);
            smsUserMaintenancePlanService.save(smsUserMaintenancePlan);
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/smsUserMaintenancePlan/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request,@RequestBody SmsUserMaintenancePlan smsUserMaintenancePlan){
        try{
            // 获取当前登录用户
            String userId=TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsUserMaintenancePlan _smsUserMaintenancePlan = smsUserMaintenancePlanService.getById(smsUserMaintenancePlan.getId());
            if(null==_smsUserMaintenancePlan){
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult=ValiDatedUtils.valiDatedBean(smsUserMaintenancePlan);
            if(!validatedResult.isStatus()){
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsUserMaintenancePlan.setUpdateUser(userId);
            _smsUserMaintenancePlan.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsUserMaintenancePlan,_smsUserMaintenancePlan);
            smsUserMaintenancePlanService.update(_smsUserMaintenancePlan);
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsUserMaintenancePlan/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids){
        try{
            smsUserMaintenancePlanService.deleteByIdIn(ids.split(","));
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsUserMaintenancePlan/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request,String ids){
        try{
            String[] idStr=ids.split(",");
            // 获取当前登录用户
            String userId=TokenUtils.getUserIdByRequest(request);
            for(String id:idStr){
                // 根据ID查询数据
                SmsUserMaintenancePlan smsUserMaintenancePlan=smsUserMaintenancePlanService.getById(id);
                if(null!=smsUserMaintenancePlan){
                    // 设置删除标识为真
                    smsUserMaintenancePlan.setDelFlag(true);
                    smsUserMaintenancePlan.setUpdateDate(new Date());
                    smsUserMaintenancePlan.setUpdateUser(userId);
                    smsUserMaintenancePlanService.update(smsUserMaintenancePlan);
                }
            }
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}
