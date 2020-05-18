package com.dico.modules.interfaces;

import com.dico.enums.MaintenanceStatusEnums;
import com.dico.feign.domain.Attachments;
import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.ConstructionSafeClient;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.*;
import com.dico.modules.dto.*;
import com.dico.modules.service.*;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.TokenUtils;
import com.dico.util.ValiDatedUtils;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
@Slf4j
@RestController
@RequestMapping("equipment")
@Api(tags = "手机端设备模块", produces = "手机端设备模块Api")
public class EquipmentInterface {

    @Resource
    private DicoBaseClient dicoBaseClient;

    @Autowired
    private SmsEquipmentFaultInfoService smsEquipmentFaultInfoService;

    @Autowired
    private SmsEquipmentFaultRecordService smsEquipmentFaultRecordService;

    @Autowired
    private SmsUserMaintenancePlanService smsUserMaintenancePlanService;

    @Autowired
    private SmsMaintenanceBeforeService smsMaintenanceBeforeService;

    @Autowired
    private SmsMaintenanceBeforeItemService smsMaintenanceBeforeItemService;

    @Autowired
    private SmsMaintenanceAfterService smsMaintenanceAfterService;

    @Autowired
    private SmsEquipmentClassService smsEquipmentClassService;

    @Autowired
    private SmsEquipmentService smsEquipmentService;


    @Autowired
    private ConstructionSafeClient constructionSafeClient;

    @Autowired
    private SmsRegionsService smsRegionsService;

    @Autowired
    private SmsEquipmentFaultAssignService smsEquipmentFaultAssignService;
    /**
     * 上报故障
     * @param request
     * @param smsEquipmentFaultInfo
     * @return
     */
    @PostMapping("/reportFault")
    @ApiOperation(value = "上报故障", notes = "上报故障")
    public ResultData reportFault(HttpServletRequest request, @RequestBody SmsEquipmentFaultInfo smsEquipmentFaultInfo) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(userId)) {
            log.error("Request error with EquipmentInsterface.reportFault(), Cannot get the current user by [" + userId + "]");
            return ResultData.getFailResult("获取当前用户出错");
        }
        if(StringUtils.isBlank(smsEquipmentFaultInfo.getRemark())){
            return ResultData.getFailResult("故障描述不能为空");
        }
        smsEquipmentFaultInfo.setReportUserId(userId);
        smsEquipmentFaultInfo.setReportDate(new Date());
        smsEquipmentFaultInfo.setCreateUser(userId);
        smsEquipmentFaultInfo.setCreateDate(new Date());
        smsEquipmentFaultInfo.setAssign(false);
        smsEquipmentFaultInfo.setDelFlag(false);
        smsEquipmentFaultInfoService.save(smsEquipmentFaultInfo);
        List<Attachments> attachmentsList = dicoBaseClient.findAttachmentListByIds(smsEquipmentFaultInfo.getFileIds());
        attachmentsList.forEach(attachments -> {
            attachments.setTargetId(smsEquipmentFaultInfo.getId());
            dicoBaseClient.updateAttchment(attachments);
        });
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
    @GetMapping("/smsEquipmentClassFindTreeList")
    @ApiOperation(value = "获取设备类型树形数据", notes = "获取设备类型树形数据")
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
            List<SmsEquipment> smsEquipmentList=constructionSafeClient.findByClassId(smsEquipmentClass.getId());
//            if(smsEquipmentList.size()!=0){
//                smsEquipmentClass.setTest(smsEquipmentList.get(1).getName());
//            }

            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("parentClass_EQ", smsEquipmentClass.getId());
            queryMap.put("delFlag_EQ", false);
            List<SmsEquipmentClass> childrenClassList = smsEquipmentClassService.findAll(queryMap);
            List<SmsEquipment> list=smsEquipmentList;
            smsEquipmentClass.setSmsEquipmentList(list);
            if (null != childrenClassList && childrenClassList.size() > 0) {
                this.quoteChildrenList(childrenClassList);
                smsEquipmentClass.setChildren(childrenClassList);
            }
        });
        return smsEquipmentClassList;
    }

    /**
     * 故障列表
     * @param request
     * @return
     */
    @GetMapping("/faultList")
    @ApiOperation(value = "故障列表", notes = "故障列表")
    public ResultData faultList(HttpServletRequest request) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(userId)) {
            log.error("Request error with EquipmentInsterface.faultList(), Cannot get the current user by [" + userId + "]");
            return ResultData.getFailResult("获取当前用户出错");
        }
        List<SmsEquipmentFaultInfoDto> smsEquipmentFaultInfoDtoList = null;
        if(dicoBaseClient.isWbdw()){
            // 维保单位故障单查询
            smsEquipmentFaultInfoDtoList = smsEquipmentFaultInfoService.findByStatusAndAssignUserId(false, userId);
        }else if(dicoBaseClient.isWxxz()){
            // 维修小组故障单查询
            smsEquipmentFaultInfoDtoList = smsEquipmentFaultInfoService.findByStatus(false);
        }
        if(smsEquipmentFaultInfoDtoList!=null){
            for (SmsEquipmentFaultInfoDto smsEquipmentFaultInfoDto : smsEquipmentFaultInfoDtoList) {
                ResultData resultData = dicoBaseClient.findAttachmentListByTargetId(smsEquipmentFaultInfoDto.getId());
                if(resultData.getCode() == 0){
                    smsEquipmentFaultInfoDto.setFiles(resultData.getData());
                }
            }
            return ResultData.getSuccessResult(smsEquipmentFaultInfoDtoList);
        }
        return ResultData.getSuccessResult();

    }
    /**
     * 复查列表
     * @param request
     * @return
     */
    @GetMapping("/ReviewList")
    @ApiOperation(value = "复查列表", notes = "复查列表")
    public ResultData reviewList(HttpServletRequest request) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(userId)) {
            log.error("Request error with EquipmentInsterface.faultList(), Cannot get the current user by [" + userId + "]");
            return ResultData.getFailResult("获取当前用户出错");
        }
        List<SmsEquipmentFaultRecord> byReviewUserId = smsEquipmentFaultRecordService.getByReviewUserId(userId);
        List<SmsEquipmentFaultWillReviewDTO> smsEquipmentFaultWillReviewDTOS=new ArrayList<>();
        for (SmsEquipmentFaultRecord smsEquipmentFaultRecord:byReviewUserId){
            SmsEquipmentFaultWillReviewDTO smsEquipmentFaultWillReviewDTO=new SmsEquipmentFaultWillReviewDTO();
            SmsEquipmentFaultInfo smsEquipmentFaultInfo = smsEquipmentFaultInfoService.getById(smsEquipmentFaultRecord.getFaultInfoId());
            SmsEquipment smsEquipment = smsEquipmentService.getByIdAndDelFlagIsFalse(smsEquipmentFaultInfo.getEquipmentId());
            SmsEquipmentFaultAssign smsEquipmentFaultAssignByFaultInfoIdAndDelFlagIsFalse = smsEquipmentFaultAssignService.getSmsEquipmentFaultAssignByFaultInfoIdAndDelFlagIsFalse(smsEquipmentFaultRecord.getFaultInfoId());
            SmsRegions smsRegions = smsRegionsService.getByIdAndDelFlagIsFalse(smsEquipment.getInstallRegionsId());
            smsEquipment.setInstallRegionsName(smsRegions.getName());
            SysUser sysUser=dicoBaseClient.findUserById(smsEquipmentFaultAssignByFaultInfoIdAndDelFlagIsFalse.getFaultUserId());
            SysUser reportuser=dicoBaseClient.findUserById(smsEquipmentFaultInfo.getReportUserId());
            ResultData attachmentListByTargetId = dicoBaseClient.findAttachmentListByTargetId(smsEquipmentFaultInfo.getId());
            smsEquipmentFaultWillReviewDTO.setReportUser(reportuser.getName());
            smsEquipmentFaultWillReviewDTO.setReportDate(smsEquipmentFaultInfo.getCreateDate());
            smsEquipmentFaultWillReviewDTO.setRemark(smsEquipmentFaultInfo.getRemark());
            smsEquipmentFaultWillReviewDTO.setFiles(attachmentListByTargetId.getData());
            smsEquipmentFaultWillReviewDTO.setEquipment(smsEquipment);
            smsEquipmentFaultWillReviewDTO.setFaultUser(sysUser.getName());
            smsEquipmentFaultWillReviewDTO.setStaus(smsEquipmentFaultRecord.getStaus());
            smsEquipmentFaultWillReviewDTO.setFaultInfoId(smsEquipmentFaultInfo.getId());
            smsEquipmentFaultWillReviewDTO.setFaultRecordId(smsEquipmentFaultRecord.getId());
            smsEquipmentFaultWillReviewDTOS.add(smsEquipmentFaultWillReviewDTO);
        }
        return ResultData.getSuccessResult(smsEquipmentFaultWillReviewDTOS);
    }


    /**
     * 故障记录列表
     * @param request
     * @return
     */
    @GetMapping("/faultRecordList")
    @ApiOperation(value = "故障记录列表", notes = "故障记录列表")
    public ResultData faultRecordList(HttpServletRequest request) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(userId)) {
            log.error("Request error with EquipmentInsterface.faultList(), Cannot get the current user by [" + userId + "]");
            return ResultData.getFailResult("获取当前用户出错");
        }
        List<SmsEquipmentFaultInfoDto> smsEquipmentFaultInfoDtoList = null;
        if(dicoBaseClient.isWbdw()){
            // 维保单位故障单查询
            smsEquipmentFaultInfoDtoList = smsEquipmentFaultInfoService.findByStatusAndAssignUserId(true, userId);
        }else if(dicoBaseClient.isWxxz()){
            // 维修小组故障单查询
            smsEquipmentFaultInfoDtoList = smsEquipmentFaultInfoService.findByStatus(true);
        }
       if(smsEquipmentFaultInfoDtoList!=null){
           for (SmsEquipmentFaultInfoDto smsEquipmentFaultInfoDto : smsEquipmentFaultInfoDtoList) {
               ResultData resultData = dicoBaseClient.findAttachmentListByTargetId(smsEquipmentFaultInfoDto.getId());
               if(resultData.getCode() == 0){
                   smsEquipmentFaultInfoDto.setFiles(resultData.getData());
               }
           }
       }else {
           return ResultData.getFailResult("没有维保记录可显示");
       }
        return ResultData.getSuccessResult(smsEquipmentFaultInfoDtoList);
    }

    /**
     * 保存复查结果
     * @param request
     * @return
     */
    @PostMapping("/saveFaultRecordResult")
    @ApiOperation(value = "保存复查结果", notes = "保存复查结果")
    public ResultData saveFaultRecordResult(HttpServletRequest request, @RequestBody SmsEqumientFaultReviewResultDTO smsEqumientFaultReviewResultDTO) {
        try {
            String userId = TokenUtils.getUserIdByRequest(request);
            if(!StringUtils.isNotBlank(userId)){
                return  ResultData.getFailResult("获取当前登录人出错");
            }
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEqumientFaultReviewResultDTO);
            if(!validatedResult.isStatus()){
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            SmsEquipmentFaultRecord smsEquipmentFaultRecord = smsEquipmentFaultRecordService.getById(smsEqumientFaultReviewResultDTO.getFaultRecordId());
          if(smsEqumientFaultReviewResultDTO.getIsStatus()){
              smsEquipmentFaultRecord.setStaus(2);
              smsEquipmentFaultRecord.setRemark(smsEqumientFaultReviewResultDTO.getReviewResult());
              smsEquipmentFaultRecordService.update(smsEquipmentFaultRecord);
          }else {
              SmsEquipmentFaultInfo smsEquipmentFaultInfoServiceById = smsEquipmentFaultInfoService.getById(smsEquipmentFaultRecord.getFaultInfoId());
              smsEquipmentFaultInfoServiceById.setStatus(false);
              smsEquipmentFaultInfoServiceById.setReviewResult(smsEqumientFaultReviewResultDTO.getReviewResult());
              smsEquipmentFaultInfoService.update(smsEquipmentFaultInfoServiceById);
              smsEquipmentFaultRecordService.deleteById(smsEquipmentFaultRecord.getId());
          }
            List<Attachments> attachmentsList = dicoBaseClient.findAttachmentListByIds(smsEqumientFaultReviewResultDTO.getFileIds());
            attachmentsList.forEach(attachments -> {
                attachments.setTargetId(smsEqumientFaultReviewResultDTO.getFaultInfoId());
                dicoBaseClient.updateAttchment(attachments);
            });

        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }



    /**
     * 保存维修记录
     * @param request
     * @return
     */
    @PostMapping("/saveFaultRecord")
    @ApiOperation(value = "保存维修记录", notes = "保存维修记录")
    public ResultData saveFaultRecord(HttpServletRequest request, @RequestBody SmsEquipmentFaultRecordDto smsEquipmentFaultRecordDto) {
        try {
            String userId = TokenUtils.getUserIdByRequest(request);
            if(!StringUtils.isNotBlank(userId)){
                return  ResultData.getFailResult("获取当前登录人出错");
            }
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEquipmentFaultRecordDto);
            if(!validatedResult.isStatus()){
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            SmsEquipmentFaultRecord smsEquipmentFaultRecord = new SmsEquipmentFaultRecord();
            smsEquipmentFaultRecord.setFaultInfoId(smsEquipmentFaultRecordDto.getFaultInfoId());
            smsEquipmentFaultRecord.setResult(smsEquipmentFaultRecordDto.getResult());
            smsEquipmentFaultRecord.setCreateDate(new Date());
            smsEquipmentFaultRecord.setStaus(0);
            smsEquipmentFaultRecord.setCreateUser(userId);
            smsEquipmentFaultRecord.setDelFlag(false);
            smsEquipmentFaultRecordService.save(smsEquipmentFaultRecord);
            List<Attachments> attachmentsList = dicoBaseClient.findAttachmentListByIds(smsEquipmentFaultRecordDto.getFiles());
            for (Attachments attachments : attachmentsList) {
                attachments.setTargetId(smsEquipmentFaultRecord.getId());
                dicoBaseClient.updateAttchment(attachments);
            }
            SmsEquipmentFaultInfo smsEquipmentFaultInfo = smsEquipmentFaultInfoService.getById(smsEquipmentFaultRecord.getFaultInfoId());
            if(null != smsEquipmentFaultInfo){
                smsEquipmentFaultInfo.setStatus(true);
                smsEquipmentFaultInfo.setUpdateUser(userId);
                smsEquipmentFaultInfo.setUpdateDate(new Date());
                smsEquipmentFaultInfoService.save(smsEquipmentFaultInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 维修记录详情
     * @param request
     * @return
     */
    @GetMapping("/findFaultRecordInfo")
    @ApiOperation(value = "维修记录详情", notes = "维修记录详情")
    public ResultData findFaultRecordInfo(HttpServletRequest request, String faultInfoId) {
        Map<String, Object> dataMap = new HashMap<>();
        SmsEquipmentFaultRecord smsEquipmentFaultRecord = smsEquipmentFaultRecordService.findByFaultInfoId(faultInfoId);
        if(null == smsEquipmentFaultRecord){
            return ResultData.getFailResult("获取维修记录失败");
        }
        SysUser sysUser = dicoBaseClient.findUserById(smsEquipmentFaultRecord.getCreateUser());
        if(null == sysUser){
            dataMap.put("repairUser", "");
            dataMap.put("repairOrganization", "");
        }else{
            dataMap.put("repairUser", sysUser.getName());
            dataMap.put("repairOrganization", sysUser.getOrganizationName());
        }
        dataMap.put("finishDate", smsEquipmentFaultRecord.getCreateDate());
        dataMap.put("result", smsEquipmentFaultRecord.getResult());
        ResultData resultData = dicoBaseClient.findAttachmentListByTargetId(smsEquipmentFaultRecord.getId());
        dataMap.put("files", resultData.getData());
        return ResultData.getSuccessResult(dataMap);
    }

    /**
     * 故障追踪列表
     * @param request
     * @return
     */
    @GetMapping("/faultTrackList")
    @ApiOperation(value = "故障追踪列表", notes = "故障追踪列表")
    public ResultData faultTrackList(HttpServletRequest request) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if(!StringUtils.isNotBlank(userId)){
            return ResultData.getFailResult("获取当前登录人失败");
        }
        List<SmsEquipmentFaultInfoDto> smsEquipmentFaultInfoDtoList = smsEquipmentFaultInfoService.findByCreateByUserId(userId);
        return ResultData.getSuccessResult(smsEquipmentFaultInfoDtoList);
    }

    /**
     * 保养列表
     * @param request
     * @return
     */
    @GetMapping("/maintenanceList")
    @ApiOperation(value = "保养列表", notes = "保养列表")
    public ResultData maintenanceList(HttpServletRequest request) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if(!StringUtils.isNotBlank(userId)){
            return ResultData.getFailResult("获取当前登录人失败");
        }
        List<SmsUserMaintenanceListDto> smsMaintenanceListDtoList = smsUserMaintenancePlanService.findUserMaintenancePlanList(userId);
        return ResultData.getSuccessResult(smsMaintenanceListDtoList);
    }

    /**
     * 保养列表详情
     * @param request
     * @return
     */
    @GetMapping("/maintenanceInfo")
    @ApiOperation(value = "保养列表详情", notes = "保养列表详情")
    public ResultData maintenanceInfo(HttpServletRequest request, String maintenanceId) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if(!StringUtils.isNotBlank(userId)){
            return ResultData.getFailResult("获取当前登录人失败");
        }
        List<SmsMaintenanceTargetDto> smsMaintenanceTargetDtoList = smsUserMaintenancePlanService.findMaintenanceTarget(maintenanceId);
        return ResultData.getSuccessResult(smsMaintenanceTargetDtoList);
    }

    /**
     * 保存保养记录
     * @param request
     * @return
     */
    @Transactional
    @PostMapping("/saveMaintenanceRecord")
    @ApiOperation(value = "保存保养记录", notes = "保存保养记录")
    public ResultData saveMaintenanceRecord(HttpServletRequest request, @RequestBody SmsUserMaintenanceRecordDto smsUserMaintenanceRecord) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if(!StringUtils.isNotBlank(userId)){
            return ResultData.getFailResult("获取当前登录人失败");
        }
        SmsUserMaintenancePlan smsUserMaintenancePlan = smsUserMaintenancePlanService.getById(smsUserMaintenanceRecord.getUserMaintenancePlanId());
        if(null == smsUserMaintenancePlan){
            return ResultData.getFailResult("用户计划不存在");
        }
        SmsMaintenanceBefore smsMaintenanceBefore = new SmsMaintenanceBefore();
        smsMaintenanceBefore.setUserMaintenanceId(smsUserMaintenanceRecord.getUserMaintenancePlanId());
        smsMaintenanceBefore.setRemark(smsUserMaintenanceRecord.getRemark());
        smsMaintenanceBefore.setCreateDate(new Date());
        smsMaintenanceBefore.setCreateUser(userId);
        smsMaintenanceBefore.setDelFlag(false);
        smsMaintenanceBeforeService.save(smsMaintenanceBefore);
        List<Attachments> attachmentsList = dicoBaseClient.findAttachmentListByIds(smsUserMaintenanceRecord.getFiles());
        for (Attachments attachments : attachmentsList) {
            attachments.setTargetId(smsMaintenanceBefore.getId());
            dicoBaseClient.updateAttchment(attachments);
        }
        List<SmsMaintenanceBeforeItem> smsMaintenanceBeforeItemList = new ArrayList<>();
        for (SmsMaintenanceItemDto smsMaintenanceItemDto : smsUserMaintenanceRecord.getSmsMaintenanceItemList()) {
            SmsMaintenanceBeforeItem smsMaintenanceBeforeItem = new SmsMaintenanceBeforeItem();
            smsMaintenanceBeforeItem.setMaintenanceBeforeId(smsMaintenanceBefore.getId());
            smsMaintenanceBeforeItem.setMaintenanceTargetId(smsMaintenanceItemDto.getTargetId());
            smsMaintenanceBeforeItem.setResult(smsMaintenanceItemDto.getResult());
            smsMaintenanceBeforeItem.setCreateDate(new Date());
            smsMaintenanceBeforeItem.setCreateUser(userId);
            smsMaintenanceBeforeItemList.add(smsMaintenanceBeforeItem);
        }
        smsMaintenanceBeforeItemService.save(smsMaintenanceBeforeItemList);
        SmsMaintenanceAfter smsMaintenanceAfter = new SmsMaintenanceAfter();
        smsMaintenanceAfter.setUserMaintenanceId(smsUserMaintenanceRecord.getUserMaintenancePlanId());
        smsMaintenanceAfter.setCost(smsUserMaintenanceRecord.getSmsMaintenanceResult().getCost());
        smsMaintenanceAfter.setRemark(smsUserMaintenanceRecord.getSmsMaintenanceResult().getResult());
        smsMaintenanceAfter.setCreateDate(new Date());
        smsMaintenanceAfter.setCreateUser(userId);
        smsMaintenanceAfter.setDelFlag(false);
        smsMaintenanceAfterService.save(smsMaintenanceAfter);
        List<Attachments> afterAttachmentsList = dicoBaseClient.findAttachmentListByIds(smsUserMaintenanceRecord.getSmsMaintenanceResult().getFiles());
        for (Attachments attachments : afterAttachmentsList) {
            attachments.setTargetId(smsMaintenanceAfter.getId());
            dicoBaseClient.updateAttchment(attachments);
        }
        smsUserMaintenancePlan.setStatus(MaintenanceStatusEnums.FINISH.getKey());
        smsUserMaintenancePlan.setUpdateDate(new Date());
        smsUserMaintenancePlan.setUpdateUser(userId);
        smsUserMaintenancePlanService.save(smsUserMaintenancePlan);
        return ResultData.getSuccessResult();
    }

    /**
     * 保养记录列表
     * @param request
     * @return
     */
    @GetMapping("/maintenanceRecordList")
    @ApiOperation(value = "保养记录列表", notes = "保养记录列表")
    public ResultData maintenanceRecordList(HttpServletRequest request) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if(!StringUtils.isNotBlank(userId)){
            return ResultData.getFailResult("获取当前登录人失败");
        }
        List<SmsUserMaintenanceListDto> smsMaintenanceListDtoList = smsUserMaintenancePlanService.findUserMaintenancePlanRecordList();
        return ResultData.getSuccessResult(smsMaintenanceListDtoList);
    }

    /**
     * 保养记录详情
     * @param request
     * @return
     */
    @Transactional
    @GetMapping("/getMaintenanceRecordInfo")
    @ApiOperation(value = "保养记录详情", notes = "保养记录详情")
    public ResultData getMaintenanceRecordInfo(HttpServletRequest request, String maintenanceRecordId) {
        String userId = TokenUtils.getUserIdByRequest(request);
        if(!StringUtils.isNotBlank(userId)){
            return ResultData.getFailResult("获取当前登录人失败");
        }
        SmsMaintenanceRecordInfoDto smsMaintenanceRecordInfo = smsUserMaintenancePlanService.findRecordInfo(maintenanceRecordId);
        if(null == smsMaintenanceRecordInfo){
            return ResultData.getFailResult("保养记录不存在");
        }
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
        return ResultData.getSuccessResult(smsMaintenanceRecordInfo);
    }


}
