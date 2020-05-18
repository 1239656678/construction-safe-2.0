package com.dico.modules.interfaces;

import com.dico.common.enums.MaintenanceStatusEnums;
import com.dico.enums.ActivitiStatusEnum;
import com.dico.enums.DangerFormEnum;
import com.dico.enums.DangerStatusEnum;
import com.dico.feign.domain.Attachments;
import com.dico.feign.domain.Cost;
import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.ActivitiClient;
import com.dico.feign.feignClient.ConstructionSafeClient;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.*;
import com.dico.modules.dto.*;
import com.dico.modules.service.*;
import com.dico.result.ResultData;
import com.dico.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Api(tags = "保养模块", produces = "保养模块Api")
public class MaintainInterface {

    @Autowired
    private DicoBaseClient dicoBaseClient;

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    @Autowired
    private SmsUserMaintenancePlanReviewService smsUserMaintenancePlanReviewService;

    @Autowired
    private  SmsUserMaintenancePlanService smsUserMaintenancePlanService;

    @Autowired
    private  SmsMaintenancePlanService smsMaintenancePlanService;

    @Autowired
    private ConstructionSafeClient constructionSafeClient;

    @Autowired
    private SmsMaintenanceAfterService smsMaintenanceAfterService;

    @Autowired
    private SmsMaintenanceBeforeService smsMaintenanceBeforeService;

    @Autowired
    private SmsRegionsService smsRegionsService;


    /**
     * 保存复查信息
     *
     * @param request
     * @param
     * @return
     */
    @ResponseBody
    @Transactional
    @PostMapping("/MaintainInterface/toReview")
    @ApiOperation(value = "保存复查信息", notes = "保存复查信息")
    public ResultData toReview(HttpServletRequest request, @RequestBody SmsMaintainReviewDTO smsMaintainReviewDTO) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        SmsUserMaintenancePlan smsUserMaintenancePlan=smsUserMaintenancePlanService.getById(smsMaintainReviewDTO.getPlanId());
        SmsUserMaintenancePlanReview smsUserMaintenancePlanReview=smsUserMaintenancePlanReviewService.getById(smsMaintainReviewDTO.getMainid());
        if (null == smsUserMaintenancePlan) {
            return ResultData.getFailResult("复查单信息不存在");
        }
        if(smsMaintainReviewDTO.getIsStatus()){
            smsUserMaintenancePlan.setUpdateUser(currentUserId);
            smsUserMaintenancePlan.setStatus(MaintenanceStatusEnums.OVER.getKey());
            smsUserMaintenancePlan.setUpdateDate(new Date());
            smsUserMaintenancePlanReview.setMaintainResult(smsMaintainReviewDTO.getReviewResult());
            smsUserMaintenancePlanReview.setMaintainStatus(2);//0：待复查，1：未通过，2已通过
            smsUserMaintenancePlanReview.setUpdateUser(currentUserId);
            smsUserMaintenancePlanReview.setUpdateDate(new Date());
            smsUserMaintenancePlanReviewService.update(smsUserMaintenancePlanReview);
        }else {
            smsUserMaintenancePlan.setUpdateUser(currentUserId);
            smsUserMaintenancePlan.setStatus(MaintenanceStatusEnums.WILL.getKey());
            smsUserMaintenancePlan.setEndDate(smsMaintainReviewDTO.getMaintainLimit());
            smsUserMaintenancePlan.setRemark(smsMaintainReviewDTO.getReviewResult());
            smsUserMaintenancePlanReviewService.deleteByMainId(smsMaintainReviewDTO.getPlanId());
            // 根据ID查询数据
            List<SmsMaintenanceAfter> smsal=smsMaintenanceAfterService.getByPlanId(smsMaintainReviewDTO.getPlanId());
            if(null!=smsal){
                // 设置删除标识为真
                for(SmsMaintenanceAfter smsMaintenanceAfter:smsal){
                    smsMaintenanceAfter.setDelFlag(true);
                    smsMaintenanceAfter.setUpdateDate(new Date());
                    smsMaintenanceAfter.setUpdateUser(currentUserId);
                    smsMaintenanceAfterService.update(smsMaintenanceAfter);
                }
            }
            List<SmsMaintenanceBefore> byMainId = smsMaintenanceBeforeService.getByMainId(smsMaintainReviewDTO.getPlanId());
            if(null!=byMainId){
                // 设置删除标识为真
                for(SmsMaintenanceBefore smsMaintenanceBefore:byMainId){
                    smsMaintenanceBefore.setDelFlag(true);
                    smsMaintenanceBefore.setUpdateDate(new Date());
                    smsMaintenanceBefore.setUpdateUser(currentUserId);
                    smsMaintenanceBeforeService.update(smsMaintenanceBefore);
                }
            }
        }
        smsUserMaintenancePlanService.update(smsUserMaintenancePlan);
        List<Attachments> attachmentsList = dicoBaseClient.findAttachmentListByIds(smsMaintainReviewDTO.getFileIds());
        attachmentsList.forEach(attachments -> {
            attachments.setTargetId(smsMaintainReviewDTO.getMainid());
            dicoBaseClient.updateAttchment(attachments);
        });

        return ResultData.getSuccessResult();
    }

    /**
     * 待复查
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/MaintainInterface/willReview")
    @ApiOperation(value = "待复查", notes = "待复查")
    public ResultData willReview(HttpServletRequest request) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        List<SmsUserMaintenancePlanReview> smsUserMaintenancePlanReviewList=smsUserMaintenancePlanReviewService.getByMaintainUserIdList(currentUserId);
        List<MaintainWillReviewDTO> maintainWillReviewDTOS=new ArrayList<>();
        if(smsUserMaintenancePlanReviewList!=null){
            for (SmsUserMaintenancePlanReview smsUserMaintenancePlanReview:smsUserMaintenancePlanReviewList){
                MaintainWillReviewDTO maintainWillReviewDTO=new MaintainWillReviewDTO();
                SmsUserMaintenancePlan sump = smsUserMaintenancePlanService.getById(smsUserMaintenancePlanReview.getMaintainId());
                SysUser sysUser=dicoBaseClient.findUserById(smsUserMaintenancePlanReview.getCreateUser());
                SmsMaintenancePlan smp = smsMaintenancePlanService.getById(sump.getMaintenancePlanId());
                SmsEquipment byIdAndDelFlagIsFalse = smsEquipmentService.getByIdAndDelFlagIsFalse(smp.getEquipmentId());
                SmsRegions smsRegions = smsRegionsService.getByIdAndDelFlagIsFalse(byIdAndDelFlagIsFalse.getInstallRegionsId());
                byIdAndDelFlagIsFalse.setInstallRegionsName(smsRegions.getName());
                maintainWillReviewDTO.setMainId(smsUserMaintenancePlanReview.getId());
                maintainWillReviewDTO.setPlanId(smsUserMaintenancePlanReview.getMaintainId());
                maintainWillReviewDTO.setEquipment(byIdAndDelFlagIsFalse);
                maintainWillReviewDTO.setMaintainCreateUser(sysUser.getName());
                maintainWillReviewDTOS.add(maintainWillReviewDTO);
            }
        }
        return ResultData.getSuccessResult(maintainWillReviewDTOS);
    }


}
