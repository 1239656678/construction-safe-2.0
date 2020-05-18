package com.dico.modules.controller;

import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.*;
import com.dico.modules.dto.EquipmentFaultRecordStatistice;
import com.dico.modules.dto.SmsEquipmentFaultInfoReviewDto;
import com.dico.modules.service.*;
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

@RestController
@Api(tags = "故障模块", produces = "故障模块Api")
public class SmsEquipmentFaultInfoController {

    @Autowired
    private SmsEquipmentFaultInfoService smsEquipmentFaultInfoService;

    @Autowired
    private SmsEquipmentFaultRecordService smsEquipmentFaultRecordService;

    @Autowired
    private SmsEquipmentFaultAssignService smsEquipmentFaultAssignService;

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    @Autowired
    private SmsRegionsService smsRegionsService;

    @Autowired
    private DicoBaseClient dicoBaseClient;

    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsEquipmentFaultInfo/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsEquipmentFaultInfo> smsEquipmentFaultInfoPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsEquipmentFaultInfoPage = smsEquipmentFaultInfoService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsEquipmentFaultInfoPage);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsEquipmentFaultInfo/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            queryMap.put("status_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = smsEquipmentFaultInfoService.findAll(queryMap, sort, request);
            for (Map<String, Object> stringObjectMap : listDataMap) {
                stringObjectMap.put("equipmentCode", "");
                stringObjectMap.put("equipmentName", "");
                stringObjectMap.put("installArea", "");
                stringObjectMap.put("files", "");
                String equipmentId = null;
                if(null != stringObjectMap.get("equipmentId")){
                    equipmentId = stringObjectMap.get("equipmentId").toString();
                }
                if(StringUtils.isNotBlank(equipmentId)){
                    SmsEquipment smsEquipment = smsEquipmentService.getSmsEquipmentByIdAndDelFlagIsFalse(equipmentId);
                    if(null != smsEquipment){
                        ResultData resultData=dicoBaseClient.findAttachmentListByTargetId(stringObjectMap.get("id").toString());
                        if(null != resultData && resultData.getCode() == 0){
                            stringObjectMap.put("files", resultData.getData());
                        }
                        stringObjectMap.put("equipmentCode", smsEquipment.getCode());
                        stringObjectMap.put("equipmentName", smsEquipment.getName());
                        if(StringUtils.isNotBlank(smsEquipment.getInstallRegionsId())){
                            SmsRegions smsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(smsEquipment.getInstallRegionsId());
                            stringObjectMap.put("installArea", smsRegions.getName());
                        }else{
                            stringObjectMap.put("installArea", "");
                        }
                    }
                }
                String reportUserId = null;
                if(null != stringObjectMap.get("reportUserId")){
                    reportUserId = stringObjectMap.get("reportUserId").toString();
                }
                if(StringUtils.isNotBlank(reportUserId)){
                    SysUser sysUser = dicoBaseClient.findUserById(reportUserId);
                    if(null != sysUser){
                        stringObjectMap.put("reportUserName", sysUser.getName());
                        stringObjectMap.put("reportOrganizationName", sysUser.getOrganizationName());
                    }
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }
    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsEquipmentFaultRecord/dataReviewList")
    @ApiOperation(value = "获取待复查数据", notes = "获取待复查数据")
    public ResultData dataReviewList(HttpServletRequest request) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            queryMap.put("staus_LT",2);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
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
    @GetMapping("/smsEquipmentFaultInfo/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsEquipmentFaultInfoId) {
        SmsEquipmentFaultInfo smsEquipmentFaultInfo = smsEquipmentFaultInfoService.getById(smsEquipmentFaultInfoId);
        if (null == smsEquipmentFaultInfo) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsEquipmentFaultInfo);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/smsEquipmentFaultInfo/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsEquipmentFaultInfo smsEquipmentFaultInfo) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEquipmentFaultInfo);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsEquipmentFaultInfo.setCreateUser(userId);
            smsEquipmentFaultInfo.setCreateDate(new Date());
            smsEquipmentFaultInfo.setDelFlag(false);
            smsEquipmentFaultInfoService.save(smsEquipmentFaultInfo);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }


    /**
     * 故障指派
     */
    @ResponseBody
    @PostMapping("/smsEquipmentFaultInfo/faultReview")
    @ApiOperation(value = "故障复查", notes = "故障复查")
    public ResultData faultReview(HttpServletRequest request, @RequestBody SmsEquipmentFaultInfoReviewDto smsEquipmentFaultInfoReviewDto) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEquipmentFaultInfoReviewDto);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            SmsEquipmentFaultRecord smsEquipmentFaultRecord = smsEquipmentFaultRecordService.getById(smsEquipmentFaultInfoReviewDto.getRecordId());
            smsEquipmentFaultRecord.setStaus(1);
            smsEquipmentFaultRecord.setReviewUserId(smsEquipmentFaultInfoReviewDto.getReviewUserId());
            smsEquipmentFaultRecord.setEndDate(smsEquipmentFaultInfoReviewDto.getEndDate());
            smsEquipmentFaultRecordService.update(smsEquipmentFaultRecord);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
    /**
     * 故障指派
     */
    @ResponseBody
    @PostMapping("/smsEquipmentFaultInfo/faultAssign")
    @ApiOperation(value = "故障指派", notes = "故障指派")
    public ResultData faultAssign(HttpServletRequest request, @RequestBody SmsEquipmentFaultAssign smsEquipmentFaultAssign) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEquipmentFaultAssign);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            SmsEquipmentFaultInfo smsEquipmentFaultInfo = smsEquipmentFaultInfoService.getById(smsEquipmentFaultAssign.getFaultInfoId());
            smsEquipmentFaultAssign.setCreateUser(userId);
            smsEquipmentFaultAssign.setCreateDate(new Date());
            smsEquipmentFaultAssign.setDelFlag(false);
            smsEquipmentFaultAssignService.save(smsEquipmentFaultAssign);
            smsEquipmentFaultInfo.setAssign(true);
            smsEquipmentFaultInfo.setUpdateDate(new Date());
            smsEquipmentFaultInfo.setUpdateUser(userId);
            smsEquipmentFaultInfoService.save(smsEquipmentFaultInfo);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/smsEquipmentFaultInfo/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsEquipmentFaultInfo smsEquipmentFaultInfo) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsEquipmentFaultInfo _smsEquipmentFaultInfo = smsEquipmentFaultInfoService.getById(smsEquipmentFaultInfo.getId());
            if (null == _smsEquipmentFaultInfo) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEquipmentFaultInfo);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsEquipmentFaultInfo.setUpdateUser(userId);
            _smsEquipmentFaultInfo.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsEquipmentFaultInfo, _smsEquipmentFaultInfo);
            smsEquipmentFaultInfoService.update(_smsEquipmentFaultInfo);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsEquipmentFaultInfo/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsEquipmentFaultInfoService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsEquipmentFaultInfo/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsEquipmentFaultInfo smsEquipmentFaultInfo = smsEquipmentFaultInfoService.getById(id);
                if (null != smsEquipmentFaultInfo) {
                    // 设置删除标识为真
                    smsEquipmentFaultInfo.setDelFlag(true);
                    smsEquipmentFaultInfo.setUpdateDate(new Date());
                    smsEquipmentFaultInfo.setUpdateUser(userId);
                    smsEquipmentFaultInfoService.update(smsEquipmentFaultInfo);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}
