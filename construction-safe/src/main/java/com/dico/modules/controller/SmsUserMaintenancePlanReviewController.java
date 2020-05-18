package com.dico.modules.controller;

import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.SmsEquipment;
import com.dico.modules.domain.SmsMaintenancePlan;
import com.dico.modules.domain.SmsUserMaintenancePlan;
import com.dico.modules.domain.SmsUserMaintenancePlanReview;
import com.dico.modules.dto.SmsUserMaintenanceListDto;
import com.dico.modules.service.SmsEquipmentService;
import com.dico.modules.service.SmsMaintenancePlanService;
import com.dico.modules.service.SmsUserMaintenancePlanReviewService;
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
@Api(tags = "保养复查模块", produces = "保养复查模块Api")
public class SmsUserMaintenancePlanReviewController{

    @Autowired
    private SmsUserMaintenancePlanReviewService smsUserMaintenancePlanReviewService;

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
    @GetMapping("/smsUserMaintenancePlanReview/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum,@RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize){
        Page<SmsUserMaintenancePlanReview> smsUserMaintenancePlanReviewPage;
        try{
            Map<String, Object> queryMap=new HashMap<>(2);
            queryMap.put("delFlag_EQ",false);
            Sort sort=new Sort(Sort.Direction.DESC,"createDate");
            smsUserMaintenancePlanReviewPage = smsUserMaintenancePlanReviewService.findAll(queryMap,pageNum,pageSize,sort);
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsUserMaintenancePlanReviewPage);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsUserMaintenancePlanReview/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request){
        List<SmsUserMaintenancePlan> smsUserMaintenancePlanList = smsUserMaintenancePlanService.findAllListByMaintain();
        List<SmsUserMaintenanceListDto> smsUserMaintenanceListDtoList=new ArrayList<>();
        if(null!=smsUserMaintenancePlanList){
            for (SmsUserMaintenancePlan smsUserMaintenancePlan:smsUserMaintenancePlanList){
                SmsUserMaintenanceListDto smsUserMaintenanceListDto=new SmsUserMaintenanceListDto();
                SmsMaintenancePlan smp = smsMaintenancePlanService.getById(smsUserMaintenancePlan.getMaintenancePlanId());
                if(smp!=null){
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
                }else {
                    return ResultData.getFailResult("保养计划不存在");
                }

            }
        }
        return ResultData.getSuccessResult(smsUserMaintenanceListDtoList);
    }

    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/smsUserMaintenancePlanReview/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsUserMaintenancePlanReviewId){
        SmsUserMaintenancePlanReview smsUserMaintenancePlanReview = smsUserMaintenancePlanReviewService.getById(smsUserMaintenancePlanReviewId);
        if(null==smsUserMaintenancePlanReview){
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsUserMaintenancePlanReview);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/smsUserMaintenancePlanReview/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request,@RequestBody SmsUserMaintenancePlanReview smsUserMaintenancePlanReview){
        try{
            // 获取当前登录用户
            String userId=TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult=ValiDatedUtils.valiDatedBean(smsUserMaintenancePlanReview);
            if(!validatedResult.isStatus()){
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsUserMaintenancePlanReview.setCreateUser(userId);
            smsUserMaintenancePlanReview.setCreateDate(new Date());
            smsUserMaintenancePlanReview.setMaintainStatus(0);
            smsUserMaintenancePlanReview.setDelFlag(false);
            smsUserMaintenancePlanReviewService.save(smsUserMaintenancePlanReview);
            SmsUserMaintenancePlan smsUserMaintenancePlanServiceById = smsUserMaintenancePlanService.getById(smsUserMaintenancePlanReview.getMaintainId());
            if(smsUserMaintenancePlanServiceById!=null){
                smsUserMaintenancePlanServiceById.setStatus(2);
                smsUserMaintenancePlanService.update(smsUserMaintenancePlanServiceById);
            }

        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/smsUserMaintenancePlanReview/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request,@RequestBody SmsUserMaintenancePlanReview smsUserMaintenancePlanReview){
        try{
            // 获取当前登录用户
            String userId=TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsUserMaintenancePlanReview _smsUserMaintenancePlanReview = smsUserMaintenancePlanReviewService.getById(smsUserMaintenancePlanReview.getId());
            if(null==_smsUserMaintenancePlanReview){
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult=ValiDatedUtils.valiDatedBean(smsUserMaintenancePlanReview);
            if(!validatedResult.isStatus()){
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsUserMaintenancePlanReview.setUpdateUser(userId);
            _smsUserMaintenancePlanReview.setMaintainStatus(0);
            _smsUserMaintenancePlanReview.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsUserMaintenancePlanReview,_smsUserMaintenancePlanReview);
            smsUserMaintenancePlanReviewService.update(_smsUserMaintenancePlanReview);
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsUserMaintenancePlanReview/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids){
        try{
            smsUserMaintenancePlanReviewService.deleteByIdIn(ids.split(","));
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsUserMaintenancePlanReview/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request,String ids){
        try{
            String[] idStr=ids.split(",");
            // 获取当前登录用户
            String userId=TokenUtils.getUserIdByRequest(request);
            for(String id:idStr){
                // 根据ID查询数据
                SmsUserMaintenancePlanReview smsUserMaintenancePlanReview=smsUserMaintenancePlanReviewService.getById(id);
                if(null!=smsUserMaintenancePlanReview){
                    // 设置删除标识为真
                    smsUserMaintenancePlanReview.setDelFlag(true);
                    smsUserMaintenancePlanReview.setUpdateDate(new Date());
                    smsUserMaintenancePlanReview.setUpdateUser(userId);
                    smsUserMaintenancePlanReviewService.update(smsUserMaintenancePlanReview);
                }
            }
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}
