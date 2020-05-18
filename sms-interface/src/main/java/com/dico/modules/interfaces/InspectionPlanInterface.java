package com.dico.modules.interfaces;

import com.alibaba.fastjson.JSON;
import com.dico.enums.PlanStatusEnums;
import com.dico.enums.PlanTypeEnums;
import com.dico.feign.domain.Attachments;
import com.dico.feign.domain.SysUserMap;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.SmsInspectionPlan;
import com.dico.modules.domain.SmsInspectionStatus;
import com.dico.modules.domain.SmsInspectionTarget;
import com.dico.modules.domain.SmsUserInspectionPlan;
import com.dico.modules.dto.*;
import com.dico.modules.service.SmsEquipmentClassTargetService;
import com.dico.modules.service.SmsInspectionStatusService;
import com.dico.modules.service.SmsInspectionTargetService;
import com.dico.modules.service.SmsUserInspectionPlanService;
import com.dico.result.ResultData;
import com.dico.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "检查计划模块", produces = "检查计划模块Api")
public class InspectionPlanInterface {

    @Autowired
    private DicoBaseClient dicoBaseClient;

    @Autowired
    private SmsUserInspectionPlanService smsUserInspectionPlanService;

    @Autowired
    private SmsEquipmentClassTargetService smsEquipmentClassTargetService;

    @Autowired
    private SmsInspectionStatusService smsInspectionStatusService;

    @Autowired
    private SmsInspectionTargetService smsInspectionTargetService;

    /**
     * 获取我的普通检查计划
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/InspectionPlan/findInspectionPlan")
    @ApiOperation(value = "获取我的普通检查计划", notes = "获取我的普通检查计划")
    public ResultData findInspectionPlan(HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        List<InspectionPlanDTO> inspectionPlanDTOList = smsUserInspectionPlanService.findInspectionPlan(new String[]{currentUserId}, sdf.format(new Date()), false);
        inspectionPlanDTOList.forEach(inspectionPlanDTO -> {
            inspectionPlanDTO.setTypeName(PlanTypeEnums.getValue(inspectionPlanDTO.getType()));
            inspectionPlanDTO.setStatusName(PlanStatusEnums.getValue(inspectionPlanDTO.getStatus()));

        });
        return ResultData.getSuccessResult(inspectionPlanDTOList);
    }

    /**
     * 获取我的专项检查计划
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/InspectionPlan/findSpecialInspectionPlan")
    @ApiOperation(value = "获取我的专项检查计划", notes = "获取我的专项检查计划")
    public ResultData findSpecialInspectionPlan(HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        List<InspectionPlanDTO> inspectionPlanDTOList = smsUserInspectionPlanService.findInspectionPlan(new String[]{currentUserId}, sdf.format(new Date()), true);
        inspectionPlanDTOList.forEach(inspectionPlanDTO -> {
            inspectionPlanDTO.setTypeName(PlanTypeEnums.getValue(inspectionPlanDTO.getType()));
            inspectionPlanDTO.setStatusName(PlanStatusEnums.getValue(inspectionPlanDTO.getStatus()));
        });
        return ResultData.getSuccessResult(inspectionPlanDTOList);
    }

    /**
     * 获取部门下所有的普通检查计划
     *
     * @param request
     * @param organizationId
     * @return
     */
    @ResponseBody
    @GetMapping("/InspectionPlan/findInspectionPlanByOrganization")
    @ApiOperation(value = "获取部门下所有的普通检查计划", notes = "获取部门下所有的普通检查计划")
    public ResultData findInspectionPlanByOrganization(HttpServletRequest request, String organizationId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        List<SysUserMap> userList = dicoBaseClient.findUserByOrganizationId(organizationId);
        List<String> userIdList = new ArrayList();
        userList.forEach(sysUserMap -> {
            userIdList.add(sysUserMap.getId());
        });
        List<InspectionPlanDTO> inspectionPlanDTOList = smsUserInspectionPlanService.findInspectionPlan(userIdList.toArray(new String[]{}), sdf.format(new Date()), false);
        inspectionPlanDTOList.forEach(inspectionPlanDTO -> {
            inspectionPlanDTO.setTypeName(PlanTypeEnums.getValue(inspectionPlanDTO.getType()));
            inspectionPlanDTO.setStatusName(PlanStatusEnums.getValue(inspectionPlanDTO.getStatus()));
        });
        return ResultData.getSuccessResult(inspectionPlanDTOList);
    }

    /**
     * 获取部门下所有专项检查计划
     *
     * @param request
     * @param organizationId
     * @return
     */
    @ResponseBody
    @GetMapping("/InspectionPlan/findSpecialInspectionPlanByOrganization")
    @ApiOperation(value = "获取部门下所有专项检查计划", notes = "获取部门下所有专项检查计划")
    public ResultData findSpecialInspectionPlanByOrganization(HttpServletRequest request, String organizationId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        List<SysUserMap> userList = dicoBaseClient.findUserByOrganizationId(organizationId);
        List<String> userIdList = new ArrayList();
        userList.forEach(sysUserMap -> {
            userIdList.add(sysUserMap.getId());
        });
        List<InspectionPlanDTO> inspectionPlanDTOList = smsUserInspectionPlanService.findInspectionPlan(userIdList.toArray(new String[]{}), sdf.format(new Date()), true);
        inspectionPlanDTOList.forEach(inspectionPlanDTO -> {
            inspectionPlanDTO.setTypeName(PlanTypeEnums.getValue(inspectionPlanDTO.getType()));
            inspectionPlanDTO.setStatusName(PlanStatusEnums.getValue(inspectionPlanDTO.getStatus()));
        });
        return ResultData.getSuccessResult(inspectionPlanDTOList);
    }

    /**
     * 获取计划下所有需要巡检的设备
     *
     * @param request
     * @param planId
     * @return
     */
    @ResponseBody
    @GetMapping("/InspectionPlan/findInspectionEquipment")
    @ApiOperation(value = "获取计划下所有需要巡检的设备", notes = "获取计划下所有需要巡检的设备")
    public ResultData findInspectionEquipment(HttpServletRequest request, String planId, String regionId, String equipmentClassId) {
        List<InspectionEquipmentDTO> inspectionEquipmentDTOList = smsUserInspectionPlanService.findInspectionEquipment(planId, regionId, equipmentClassId);
        return ResultData.getSuccessResult(inspectionEquipmentDTOList);
    }

    /**
     * 根据设备分类ID查询巡检项
     *
     * @param request
     * @param equipmentClassId
     * @return
     */
    @ResponseBody
    @GetMapping("/InspectionPlan/findInspectionTarget")
    @ApiOperation(value = "根据设备分类ID查询巡检项", notes = "根据设备分类ID查询巡检项")
    public ResultData findInspectionTarget(HttpServletRequest request, String equipmentClassId) {
        List<InspectionTargetDTO> inspectionTargetDTOList = smsEquipmentClassTargetService.findInspectionTarget(equipmentClassId);
        return ResultData.getSuccessResult(inspectionTargetDTOList);
    }

    /**
     * 根据计划ID查询计划下的所有设备分类
     *
     * @param request
     * @param inspectionId
     * @return
     */
    @ResponseBody
    @GetMapping("/InspectionPlan/findInspectionEquipmentClass")
    @ApiOperation(value = "根据计划ID查询计划下的所有设备分类", notes = "根据计划ID查询计划下的所有设备分类")
    public ResultData findInspectionEquipmentClass(HttpServletRequest request, String inspectionId) {
        List<EquipmentClassDTO> equipmentClassDTOList = smsUserInspectionPlanService.findInspectionEquipmentClass(inspectionId);
        return ResultData.getSuccessResult(equipmentClassDTOList);
    }

    /**
     * 保存检查记录
     *
     * @param request
     * @param inspectionResult
     * @return
     */
    @ResponseBody
    @PostMapping("/InspectionPlan/saveInspectionResult")
    @ApiOperation(value = "保存检查记录", notes = "保存检查记录")
    public ResultData saveInspectionResult(HttpServletRequest request, @RequestBody InspectionResultDTO inspectionResult) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        List<TargetResultDTO> targetResultDTOList = inspectionResult.getResultList();
        String targetResultStr = JSON.toJSONString(targetResultDTOList);
        SmsInspectionStatus smsInspectionStatus = smsInspectionStatusService.getByIdAndDelFlagIsFalse(inspectionResult.getStatusId());
        smsInspectionStatus.setInstructions(inspectionResult.getInstructions());
        smsInspectionStatus.setResult(targetResultStr);
        smsInspectionStatus.setStatus(PlanStatusEnums.FINISH.getKey());
        smsInspectionStatus.setUpdateDate(new Date());
        smsInspectionStatus.setUpdateUser(currentUserId);
        smsInspectionStatusService.save(smsInspectionStatus);
        List<Attachments> attachmentsList = dicoBaseClient.findAttachmentListByIds(inspectionResult.getFileIds());
        attachmentsList.forEach(attachments -> {
            attachments.setTargetId(smsInspectionStatus.getId());
            dicoBaseClient.updateAttchment(attachments);
        });
        List<InspectionEquipmentDTO> inspectionEquipmentDTOList = smsUserInspectionPlanService.findInspectionEquipment(smsInspectionStatus.getUserInspectionPlanId(), null, null);
        if (null == inspectionEquipmentDTOList || inspectionEquipmentDTOList.size() == 0) {
            SmsUserInspectionPlan smsUserInspectionPlan = smsUserInspectionPlanService.getByIdAndDelFlagIsFalse(smsInspectionStatus.getUserInspectionPlanId());
            smsUserInspectionPlan.setStatus(PlanStatusEnums.FINISH.getKey());
            smsUserInspectionPlanService.save(smsUserInspectionPlan);
        } else {
            SmsUserInspectionPlan smsUserInspectionPlan = smsUserInspectionPlanService.getByIdAndDelFlagIsFalse(smsInspectionStatus.getUserInspectionPlanId());
            smsUserInspectionPlan.setStatus(PlanStatusEnums.DOING.getKey());
            smsUserInspectionPlanService.save(smsUserInspectionPlan);
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 检查计划记录
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/InspectionPlan/inspectionPlanRecord")
    @ApiOperation(value = "检查计划记录", notes = "检查计划记录")
    public ResultData inspectionPlanRecord(HttpServletRequest request, String datetime) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        String[] userIds = new String[]{currentUserId};
        List<InspectionPlanDTO> inspectionPlanDTOList = smsUserInspectionPlanService.findInspectionPlanRecord(userIds, datetime);
        inspectionPlanDTOList.forEach(inspectionPlanDTO -> {
            inspectionPlanDTO.setStatusName(PlanStatusEnums.getValue(inspectionPlanDTO.getStatus()));
            inspectionPlanDTO.setTypeName(PlanTypeEnums.getValue(inspectionPlanDTO.getType()));
        });
        return ResultData.getSuccessResult(inspectionPlanDTOList);
    }

    /**
     * 根据计划ID查询检查记录
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/InspectionPlan/inspectionRecordByPlanId")
    @ApiOperation(value = "根据计划ID查询检查记录", notes = "根据计划ID查询检查记录")
    public ResultData inspectionRecordByPlanId(HttpServletRequest request, String planId) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        String[] userIds = new String[]{currentUserId};
        List<InspectionRecordDTO> inspectionRecordDTOList = smsUserInspectionPlanService.findInspectionRecordByPlanId(planId);
        for (InspectionRecordDTO inspectionRecordDTO : inspectionRecordDTOList) {
            ResultData resultData = dicoBaseClient.findAttachmentListByTargetId(inspectionRecordDTO.getStatusId());
            if (resultData.getCode() == 0) {
                inspectionRecordDTO.setFiles(resultData.getData());
                List<TargetResultDTO> targetResultDTOList = JSON.parseArray(inspectionRecordDTO.getResult(), TargetResultDTO.class);
                for (TargetResultDTO targetResultDTO : targetResultDTOList) {
                    SmsInspectionTarget smsInspectionTarget = smsInspectionTargetService.getByIdAndDelFlagIsFalse(targetResultDTO.getId());
                    if (null != smsInspectionTarget) {
                        targetResultDTO.setId(smsInspectionTarget.getTargetName());
                    }
                }
                inspectionRecordDTO.setResultList(targetResultDTOList);
            }
        }
        return ResultData.getSuccessResult(inspectionRecordDTOList);
    }

}
