package com.dico.modules.interfaces;

import com.dico.enums.ActivitiStatusEnum;
import com.dico.enums.DangerFormEnum;
import com.dico.enums.DangerStatusEnum;
import com.dico.feign.domain.Attachments;
import com.dico.feign.domain.Cost;
import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.ActivitiClient;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "隐患模块", produces = "隐患模块Api")
public class DangerInfoInterface {

    @Autowired
    private DicoBaseClient dicoBaseClient;

    @Autowired
    private ActivitiClient activitiClient;

    @Autowired
    private SmsDangerInfoService smsDangerInfoService;

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    @Autowired
    private SmsRegionsService smsRegionsService;

    @Autowired
    private SmsDangerRepairService smsDangerRepairService;

    @Autowired
    private SmsDangerReviewService smsDangerReviewService;

    @Autowired
    private SmsInspectionStatusService smsInspectionStatusService;

    /**
     * 随手拍
     *
     * @param request
     * @param dangerInfo
     * @return
     */
    @ResponseBody
    @PostMapping("/DangerInfoInterface/freeShoot")
    @ApiOperation(value = "随手拍", notes = "随手拍")
    public ResultData freeShoot(HttpServletRequest request, @RequestBody DangerInfoDTO dangerInfo) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        SmsDangerInfo smsDangerInfo = new SmsDangerInfo();
        smsDangerInfo.setDangerAddress(dangerInfo.getAddress());
        smsDangerInfo.setDangerFrom(DangerFormEnum.PHOTO.getKey());
        smsDangerInfo.setDangerStatus(DangerStatusEnum.ZERO.getKey());
        smsDangerInfo.setRemark(dangerInfo.getRemark());
        smsDangerInfo.setCreateDate(new Date());
        smsDangerInfo.setCreateUser(currentUserId);
        smsDangerInfo.setDelFlag(false);
        smsDangerInfoService.save(smsDangerInfo);
        List<Attachments> attachmentsList = dicoBaseClient.findAttachmentListByIds(dangerInfo.getFileIds());
        attachmentsList.forEach(attachments -> {
            attachments.setTargetId(smsDangerInfo.getId());
            dicoBaseClient.updateAttchment(attachments);
        });
        return ResultData.getSuccessResult();
    }

    /**
     * 隐患上报
     *
     * @param request
     * @param dangerInfo
     * @return
     */
    @ResponseBody
    @Transactional
    @PostMapping("/DangerInfoInterface/reportDanger")
    @ApiOperation(value = "隐患上报", notes = "隐患上报")
    public ResultData reportDanger(HttpServletRequest request, @RequestBody DangerInfoDTO dangerInfo) {
        try {
            String currentUserId = TokenUtils.getUserIdByRequest(request);
            if (StringUtils.isBlank(currentUserId)) {
                return ResultData.getFailResult("获取当前用户出错");
            }
            SmsDangerInfo smsDangerInfo = new SmsDangerInfo();
            if(StringUtils.isNotBlank(dangerInfo.getEquipmentId())){
                SmsEquipment smsEquipment = smsEquipmentService.getByIdAndDelFlagIsFalse(dangerInfo.getEquipmentId());
                SmsRegions childrenRegions = smsRegionsService.getByIdAndDelFlagIsFalse(smsEquipment.getInstallRegionsId());
                String address = childrenRegions.getName();
                while (null != childrenRegions && StringUtils.isNotBlank(childrenRegions.getPid())) {
                    childrenRegions = smsRegionsService.getByIdAndDelFlagIsFalse(childrenRegions.getPid());
                    if (null != childrenRegions) {
                        address = childrenRegions.getName() + address;
                    }
                }
                smsDangerInfo.setEquipmentId(dangerInfo.getEquipmentId());
                smsDangerInfo.setDangerAddress(address + " " + dangerInfo.getAddress());
            }else{
                smsDangerInfo.setDangerAddress(dangerInfo.getAddress());
            }
            smsDangerInfo.setDangerFrom(DangerFormEnum.PLAN.getKey());
            smsDangerInfo.setDangerStatus(DangerStatusEnum.REPAIR.getKey());
            smsDangerInfo.setRemark(dangerInfo.getRemark());
            smsDangerInfo.setCreateDate(new Date());
            smsDangerInfo.setCreateUser(currentUserId);
            smsDangerInfo.setDelFlag(false);
            smsDangerInfoService.save(smsDangerInfo);
            List<Attachments> attachmentsList = dicoBaseClient.findAttachmentListByIds(dangerInfo.getFileIds());
            attachmentsList.forEach(attachments -> {
                attachments.setTargetId(smsDangerInfo.getId());
                dicoBaseClient.updateAttchment(attachments);
            });
            this.saveRepairAndReview(smsDangerInfo, dangerInfo, currentUserId);
        } catch (RuntimeException e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();

    }

    private void saveRepairAndReview(SmsDangerInfo smsDangerInfo, DangerInfoDTO dangerInfo, String currentUserId) {
        SmsDangerRepair smsDangerRepair = new SmsDangerRepair();
        smsDangerRepair.setDangerId(smsDangerInfo.getId());
        smsDangerRepair.setRepairLimit(dangerInfo.getRepairLimit());
        smsDangerRepair.setRepairOpinion(dangerInfo.getRepairOpinion());
        SysUser repairUser = dicoBaseClient.findUserById(dangerInfo.getRepairUserID());
        if (null == repairUser) {
            throw new RuntimeException("整改人不存在");
        }
        smsDangerRepair.setRepairOrganizationId(repairUser.getOrganizationId());
        smsDangerRepair.setRepairUserId(repairUser.getId());
        // 整改状态 true已完成false待整改
        smsDangerRepair.setRepairStatus(false);
        smsDangerRepair.setCreateDate(new Date());
        smsDangerRepair.setCreateUser(currentUserId);
        smsDangerRepair.setDelFlag(false);
        smsDangerRepairService.save(smsDangerRepair);

        SysUser revewUser = dicoBaseClient.findUserById(dangerInfo.getReviewUserID());
        if (null == revewUser) {
            throw new RuntimeException("复查人不存在");
        }
        SmsDangerReview smsDangerReview = new SmsDangerReview();
        smsDangerReview.setDangerId(smsDangerInfo.getId());
        smsDangerReview.setReviewOrganizationId(revewUser.getOrganizationId());
        smsDangerReview.setReviewUserId(revewUser.getId());
        // 复查状态 true已完成false待复查
        smsDangerReview.setReviewStatus(false);
        smsDangerReview.setCreateDate(new Date());
        smsDangerReview.setCreateUser(currentUserId);
        smsDangerReview.setDelFlag(false);
        smsDangerReviewService.save(smsDangerReview);
    }

    /**
     * 当前用户代办任务
     *
     * @param request
     * @param toDoType{1待巡检2待整改3待复查，默认全查}
     * @return
     */
    @ResponseBody
    @GetMapping("/DangerInfoInterface/findToDoList")
    @ApiOperation(value = "当前用户代办任务", notes = "当前用户代办任务")
    public ResultData findToDoList(HttpServletRequest request, @RequestParam(name = "toDoType", defaultValue = "0") int toDoType) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("rectify", null);
        dataMap.put("review", null);
        dataMap.put("inspect", null);
        if (toDoType == 0 || toDoType == 1) {
            // 查询待巡检
            List<WillInspectionDTO> willInspectionDTOList = smsInspectionStatusService.findCurrentUserInspection(currentUserId, sdf.format(new Date()));
            dataMap.put("inspect", willInspectionDTOList);
        }
        if (toDoType == 0 || toDoType == 2) {
            // 查询待整改
            List<WillRepairDTO> willRepairDTOList = smsDangerRepairService.findCurrentUserRepair(currentUserId);
            dataMap.put("rectify", willRepairDTOList);
        }
        if (toDoType == 0 || toDoType == 3) {
            // 查询待复查
            List<WillReviewDTO> willReviewDTOList = smsDangerReviewService.findCurrentUserReview(currentUserId);
            dataMap.put("review", willReviewDTOList);
        }
        return ResultData.getSuccessResult(dataMap);
    }

    /**
     * 获取整改单详情
     *
     * @param request
     * @param repairId
     * @return
     */
    @ResponseBody
    @GetMapping("/DangerInfoInterface/getRepairInfo")
    @ApiOperation(value = "获取整改单详情", notes = "获取整改单详情")
    public ResultData getRepairInfo(HttpServletRequest request, String repairId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SmsDangerRepair smsDangerRepair = smsDangerRepairService.getById(repairId);
        SmsDangerInfo smsDangerInfo = smsDangerInfoService.getById(smsDangerRepair.getDangerId());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", smsDangerRepair.getId());
        dataMap.put("dangerLevel", smsDangerInfo.getDangerLevelId());
        dataMap.put("dangerAddress", smsDangerInfo.getDangerAddress());
        SysUser sysUser = dicoBaseClient.findUserById(smsDangerInfo.getCreateUser());
        dataMap.put("inspectionUser", sysUser.getName());
        dataMap.put("repairLimit", sdf.format(smsDangerRepair.getRepairLimit()));
        dataMap.put("files", null);
        ResultData resultData = dicoBaseClient.findAttachmentListByTargetId(smsDangerInfo.getId());
        if (resultData.getCode() == 0) {
            dataMap.put("files", resultData.getData());
        }
        return ResultData.getSuccessResult(dataMap);
    }

    /**
     * 获取复查单详情
     *
     * @param request
     * @param reviewId
     * @return
     */
    @ResponseBody
    @GetMapping("/DangerInfoInterface/getReviewInfo")
    @ApiOperation(value = "获取复查单详情", notes = "获取复查单详情")
    public ResultData getReviewInfo(HttpServletRequest request, String reviewId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SmsDangerReview smsDangerReview = smsDangerReviewService.getById(reviewId);
        SmsDangerInfo smsDangerInfo = smsDangerInfoService.getById(smsDangerReview.getDangerId());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", smsDangerReview.getId());
        dataMap.put("dangerLevel", smsDangerInfo.getDangerLevelId());
        dataMap.put("dangerAddress", smsDangerInfo.getDangerAddress());
        SysUser sysUser = dicoBaseClient.findUserById(smsDangerInfo.getCreateUser());
        dataMap.put("inspectionUser", sysUser.getName());
        dataMap.put("equipmentId", smsDangerInfo.getEquipmentId());
        dataMap.put("remark", smsDangerInfo.getRemark());
        SmsDangerRepair smsDangerRepair = smsDangerRepairService.getByDangerId(smsDangerInfo.getId());
        dataMap.put("repairUserId", smsDangerRepair.getRepairUserId());
        dataMap.put("files", null);
        ResultData resultData = dicoBaseClient.findAttachmentListByTargetId(smsDangerInfo.getId());
        if (resultData.getCode() == 0) {
            dataMap.put("files", resultData.getData());
        }
        return ResultData.getSuccessResult(dataMap);
    }

    /**
     * 需要整改费用时发起申请费用流程
     *
     * @param request
     * @param repairId
     * @param costMoney
     * @return
     */
    @ResponseBody
    @PostMapping("/DangerInfoInterface/startActiviti")
    @ApiOperation(value = "发起申请费用流程", notes = "发起申请费用流程")
    public ResultData startActiviti(HttpServletRequest request, String repairId, int costMoney) {
        String userId = TokenUtils.getUserIdByRequest(request);
        Cost cost = new Cost();
        cost.setCostMoney(costMoney);
        cost.setRepairId(repairId);
        cost.setType(1);
        ResultData resultData = activitiClient.startActiviti(cost);
        if(resultData.getCode() == 0){
            SmsDangerRepair smsDangerRepair = smsDangerRepairService.getById(repairId);
            smsDangerRepair.setHasCost(true);
            smsDangerRepair.setActivitiStatus(ActivitiStatusEnum.DOING.getKey());
            smsDangerRepair.setUpdateUser(userId);
            smsDangerRepair.setUpdateDate(new Date());
            smsDangerRepairService.update(smsDangerRepair);
            return ResultData.getSuccessResult();
        }
        return ResultData.getFailResult("发起流程失败");
    }

    /**
     * 保存整改信息
     *
     * @param request
     * @param smsDangerRepair
     * @return
     */
    @ResponseBody
    @PostMapping("/DangerInfoInterface/toRepair")
    @ApiOperation(value = "保存整改信息", notes = "保存整改信息")
    public ResultData toRepair(HttpServletRequest request, @RequestBody SmsDangerRepairDTO smsDangerRepair) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        SmsDangerRepair _smsDangerRepair = smsDangerRepairService.getById(smsDangerRepair.getId());
        if (null == _smsDangerRepair) {
            return ResultData.getFailResult("整改单信息不存在");
        }
        _smsDangerRepair.setRepairResult(smsDangerRepair.getRepairResult());
        _smsDangerRepair.setHasCost(smsDangerRepair.isHasCost());
        _smsDangerRepair.setUpdateUser(currentUserId);
        _smsDangerRepair.setUpdateDate(new Date());
        _smsDangerRepair.setRepairStatus(true);
        smsDangerRepairService.update(_smsDangerRepair);
        List<Attachments> attachmentsList = dicoBaseClient.findAttachmentListByIds(smsDangerRepair.getFileIds());
        attachmentsList.forEach(attachments -> {
            attachments.setTargetId(_smsDangerRepair.getId());
            dicoBaseClient.updateAttchment(attachments);
        });
        SmsDangerInfo smsDangerInfo = smsDangerInfoService.getById(_smsDangerRepair.getDangerId());
        smsDangerInfo.setDangerStatus(DangerStatusEnum.REVIEW.getKey());
        smsDangerInfo.setUpdateUser(currentUserId);
        smsDangerInfo.setUpdateDate(new Date());
        smsDangerInfoService.update(smsDangerInfo);
        return ResultData.getSuccessResult();
    }

    /**
     * 保存复查信息
     *
     * @param request
     * @param smsDangerReview
     * @return
     */
    @ResponseBody
    @PostMapping("/DangerInfoInterface/toReview")
    @ApiOperation(value = "保存复查信息", notes = "保存复查信息")
    public ResultData toReview(HttpServletRequest request, @RequestBody SmsDangerReviewDTO smsDangerReview) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        SmsDangerReview _smsDangerReview = smsDangerReviewService.getById(smsDangerReview.getId());
        if (null == _smsDangerReview) {
            return ResultData.getFailResult("复查单信息不存在");
        }
        _smsDangerReview.setReviewResult(smsDangerReview.getReviewResult());
        _smsDangerReview.setUpdateUser(currentUserId);
        _smsDangerReview.setUpdateDate(new Date());
        _smsDangerReview.setReviewStatus(true);
        smsDangerReviewService.update(_smsDangerReview);
        List<Attachments> attachmentsList = dicoBaseClient.findAttachmentListByIds(smsDangerReview.getFileIds());
        attachmentsList.forEach(attachments -> {
            attachments.setTargetId(_smsDangerReview.getId());
            dicoBaseClient.updateAttchment(attachments);
        });
        SmsDangerInfo smsDangerInfo = smsDangerInfoService.getById(_smsDangerReview.getDangerId());
        smsDangerInfo.setDangerStatus(DangerStatusEnum.SUCCESS.getKey());
        smsDangerInfo.setUpdateUser(currentUserId);
        smsDangerInfo.setUpdateDate(new Date());
        smsDangerInfoService.update(smsDangerInfo);
        return ResultData.getSuccessResult();
    }

    /**
     * 待整改
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/DangerInfoInterface/willRepair")
    @ApiOperation(value = "待整改", notes = "待整改")
    public ResultData willRepair(HttpServletRequest request) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        // 查询待整改
        List<WillRepairDTO> willRepairDTOList = smsDangerRepairService.findCurrentUserRepair(currentUserId);
        return ResultData.getSuccessResult(willRepairDTOList);
    }

    /**
     * 待复查
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/DangerInfoInterface/willReview")
    @ApiOperation(value = "待复查", notes = "待复查")
    public ResultData willReview(HttpServletRequest request) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        // 查询待复查
        List<WillReviewDTO> willReviewDTOList = smsDangerReviewService.findCurrentUserReview(currentUserId);
        return ResultData.getSuccessResult(willReviewDTOList);
    }

    /**
     * 超期隐患
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/DangerInfoInterface/outDanger")
    @ApiOperation(value = "超期隐患", notes = "超期隐患")
    public ResultData outDanger(HttpServletRequest request) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if (StringUtils.isBlank(currentUserId)) {
            return ResultData.getFailResult("获取当前用户出错");
        }
        // 查询超期隐患
        List<WillRepairDTO> willRepairDTOList = smsDangerRepairService.findCurrentUserOutRepair(currentUserId);
        return ResultData.getSuccessResult(willRepairDTOList);
    }


}
