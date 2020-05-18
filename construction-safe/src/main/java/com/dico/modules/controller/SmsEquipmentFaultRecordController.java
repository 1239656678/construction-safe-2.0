package com.dico.modules.controller;

import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.SmsEquipment;
import com.dico.modules.domain.SmsEquipmentFaultInfo;
import com.dico.modules.domain.SmsEquipmentFaultRecord;
import com.dico.modules.domain.SmsRegions;
import com.dico.modules.dto.EquipmentFaultRecordStatistice;
import com.dico.modules.service.SmsEquipmentFaultInfoService;
import com.dico.modules.service.SmsEquipmentFaultRecordService;
import com.dico.modules.service.SmsEquipmentService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

@RestController
@Api(tags = "故障维修记录模块", produces = "故障维修记录模块Api")
public class SmsEquipmentFaultRecordController {

    @Autowired
    private SmsEquipmentFaultRecordService smsEquipmentFaultRecordService;
    @Autowired
    private SmsEquipmentFaultInfoService smsEquipmentFaultInfoService;
    @Autowired
    private DicoBaseClient dicoBaseClient;
    @Autowired
    private SmsEquipmentService smsEquipmentService;
    @Autowired
    private SmsRegionsService smsRegionsService;



    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsEquipmentFaultRecord/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsEquipmentFaultRecord> smsEquipmentFaultRecordPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsEquipmentFaultRecordPage = smsEquipmentFaultRecordService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsEquipmentFaultRecordPage);
    }


    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsEquipmentFaultRecord/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            queryMap.put("staus_GT", 1);
            queryMap.put("endDate_LT", new Date());
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            List<Map<String, Object>> updatalistDataMap = smsEquipmentFaultRecordService.findAll(queryMap, sort, request);
            for(Map<String, Object> stringObjectMap : updatalistDataMap){
                SmsEquipmentFaultRecord status = smsEquipmentFaultRecordService.getById(stringObjectMap.get("id").toString());
                if(status.getStaus()!=2 && status!=null){
                    status.setStaus(3);
                    smsEquipmentFaultRecordService.update(status);
                }
            }
            listDataMap = smsEquipmentFaultRecordService.findAll(queryMap, sort, request);
            for(Map<String, Object> stringObjectMap : listDataMap){
                    stringObjectMap.put("equipmentFaultRecordStatistice",null);

                    EquipmentFaultRecordStatistice equipmentFaultRecordStatistice=new EquipmentFaultRecordStatistice();
                        SmsEquipmentFaultInfo smsEquipmentFaultInfo= smsEquipmentFaultInfoService.getById(stringObjectMap.get("faultInfoId").toString());
                        if(null != smsEquipmentFaultInfo) {
                            SysUser reportUser=dicoBaseClient.findUserById(smsEquipmentFaultInfo.getReportUserId());
                            equipmentFaultRecordStatistice.setReportRemark(smsEquipmentFaultInfo.getRemark());
                            if(null != reportUser){
                                equipmentFaultRecordStatistice.setReportUser(reportUser.getName());
                                equipmentFaultRecordStatistice.setReportDate(reportUser.getCreateDate().toString());
                            }
                            String equipmentId=smsEquipmentFaultInfo.getEquipmentId();
                            if(StringUtils.isNotBlank(equipmentId)){
                                SmsEquipment smsEquipment = smsEquipmentService.getSmsEquipmentByIdAndDelFlagIsFalse(equipmentId);
                                if(null != smsEquipment){
                                    ResultData faultFile=dicoBaseClient.findAttachmentListByTargetId(stringObjectMap.get("faultInfoId").toString());
                                    ResultData reportFiles=dicoBaseClient.findAttachmentListByTargetId(stringObjectMap.get("id").toString());
                                    if(null != faultFile && faultFile.getCode() == 0){
                                        equipmentFaultRecordStatistice.setFaultFiles(faultFile.getData());
                                    }
                                    if(null != reportFiles && reportFiles.getCode() == 0){
                                        equipmentFaultRecordStatistice.setReportFiles(reportFiles.getData());
                                    }
                                    equipmentFaultRecordStatistice.setEquipmentCode(smsEquipment.getCode());
                                    equipmentFaultRecordStatistice.setEquipmentName(smsEquipment.getName());
                                    equipmentFaultRecordStatistice.setEquipmentType(smsEquipment.getTypeName());
                                    equipmentFaultRecordStatistice.setEquipmentModel(smsEquipment.getModel());
                                    equipmentFaultRecordStatistice.setEquipmentRemark(smsEquipment.getRemark());
                                    equipmentFaultRecordStatistice.setProduceDate(smsEquipment.getProduceDate());
                                    equipmentFaultRecordStatistice.setScrappedDate(smsEquipment.getScrappedDate());
                                    equipmentFaultRecordStatistice.setResponsibleOrganizationName(smsEquipment.getResponsibleOrganizationName());
                                    equipmentFaultRecordStatistice.setResponsibleUser(smsEquipment.getResponsibleUserName());
                                    if(null!=stringObjectMap.get("result")){
                                        equipmentFaultRecordStatistice.setFaultResult(stringObjectMap.get("result").toString());
                                    }
                                    if(StringUtils.isNotBlank(smsEquipment.getInstallRegionsId())){
                                        SmsRegions smsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(smsEquipment.getInstallRegionsId());
                                        equipmentFaultRecordStatistice.setInstallArea(smsRegions.getName());
                                    }
                                }
                            }
                            SysUser sysUser=dicoBaseClient.findUserById(stringObjectMap.get("createUser").toString());
                            if(null != sysUser){
                                equipmentFaultRecordStatistice.setFaultUser(sysUser.getName());
                                equipmentFaultRecordStatistice.setFaultUserOrganizationName(sysUser.getOrganizationName());
                            }
                            stringObjectMap.put("equipmentFaultRecordStatistice",equipmentFaultRecordStatistice);
                        }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/smsEquipmentFaultRecord/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsEquipmentFaultRecordId) {
        SmsEquipmentFaultRecord smsEquipmentFaultRecord = smsEquipmentFaultRecordService.getById(smsEquipmentFaultRecordId);
        if (null == smsEquipmentFaultRecord) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsEquipmentFaultRecord);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/smsEquipmentFaultRecord/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsEquipmentFaultRecord smsEquipmentFaultRecord) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEquipmentFaultRecord);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsEquipmentFaultRecord.setCreateUser(userId);
            smsEquipmentFaultRecord.setCreateDate(new Date());
            smsEquipmentFaultRecord.setDelFlag(false);
            smsEquipmentFaultRecordService.save(smsEquipmentFaultRecord);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/smsEquipmentFaultRecord/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsEquipmentFaultRecord smsEquipmentFaultRecord) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsEquipmentFaultRecord _smsEquipmentFaultRecord = smsEquipmentFaultRecordService.getById(smsEquipmentFaultRecord.getId());
            if (null == _smsEquipmentFaultRecord) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEquipmentFaultRecord);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsEquipmentFaultRecord.setUpdateUser(userId);
            _smsEquipmentFaultRecord.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsEquipmentFaultRecord, _smsEquipmentFaultRecord);
            smsEquipmentFaultRecordService.update(_smsEquipmentFaultRecord);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsEquipmentFaultRecord/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsEquipmentFaultRecordService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsEquipmentFaultRecord/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsEquipmentFaultRecord smsEquipmentFaultRecord = smsEquipmentFaultRecordService.getById(id);
                if (null != smsEquipmentFaultRecord) {
                    // 设置删除标识为真
                    smsEquipmentFaultRecord.setDelFlag(true);
                    smsEquipmentFaultRecord.setUpdateDate(new Date());
                    smsEquipmentFaultRecord.setUpdateUser(userId);
                    smsEquipmentFaultRecordService.update(smsEquipmentFaultRecord);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}
