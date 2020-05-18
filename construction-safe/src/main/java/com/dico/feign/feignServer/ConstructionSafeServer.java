package com.dico.feign.feignServer;

import com.dico.feign.domain.OrganizationFeignDomain;
import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.*;
import com.dico.modules.dto.SmsEquipmentDto;
import com.dico.modules.dto.SmsEquipmentFaultInfoDto;
import com.dico.modules.dto.SmsMaintenanceRecordInfoDto;
import com.dico.modules.dto.SmsUserMaintenanceListDto;
import com.dico.modules.service.*;
import com.dico.result.ResultData;
import com.dico.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-04-29 11:10
 */
@Slf4j
@RestController
@RequestMapping(value = "ConstructionSafeServer")
@Api(tags = "微服务调模块", produces = "微服务调模块Api")
public class ConstructionSafeServer {

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    @Autowired
    private SmsEquipmentClassService smsEquipmentClassService;

    @Autowired
    private SmsUserMaintenancePlanService smsUserMaintenancePlanService;

    @Autowired
    private SmsMaintenancePlanService smsMaintenancePlanService;

    @Autowired
    private DicoBaseClient dicoBaseClient;

    @Autowired
    private SmsRegionsService smsRegionsService;

    @Autowired
    private SmsEquipmentFaultInfoService smsEquipmentFaultInfoService;

    /**
     * 根据设备分类ID查询设备
     * @param request
     * @param classId
     * @return
     */
    @ResponseBody
    @GetMapping("/smsEquipmentFindByClassId")
    @ApiOperation(value = "根据设备分类ID查询设备", notes = "根据设备分类ID查询设备")
    public List<SmsEquipment> findByClassId(HttpServletRequest request, @RequestParam String classId) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        List<SmsEquipmentClass> smsEquipmentClassList = smsEquipmentClassService.findChildrens(classId);
        List<String> idList = new ArrayList<>();
        this.getId(smsEquipmentClassList, idList);
        String[] ids = new String[idList.size()];
        idList.toArray(ids);
        return smsEquipmentService.findByTypeIdIn(ids);
    }

    private List<String> getId(List<SmsEquipmentClass> smsEquipmentClassList, List<String> idList){
        for (SmsEquipmentClass smsEquipmentClass : smsEquipmentClassList) {
            idList.add(smsEquipmentClass.getId());
            List<SmsEquipmentClass> childrens = smsEquipmentClass.getChildren();
            if(null != childrens && childrens.size() > 0){
                this.getId(childrens, idList);
            }
        }
        return idList;
    }



    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/smsEquipmentDataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public SmsEquipment smsEquipmentDataInfo(String smsEquipmentId) {
        SmsEquipment smsEquipment = smsEquipmentService.getSmsEquipmentByIdAndDelFlagIsFalse(smsEquipmentId);
        if (null != smsEquipment) {
            SmsRegions smsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(smsEquipment.getInstallRegionsId());
            smsEquipment.setInstallRegionsId(smsRegions.getName());
            SmsEquipmentClass smsEquipmentClass = smsEquipmentClassService.getSmsEquipmentClassByIdAndDelFlagIsFalse(smsEquipment.getTypeId());
            smsEquipment.setTypeName(smsEquipmentClass.getClassName());
            SysUser sysUser = dicoBaseClient.findUserById(smsEquipment.getResponsibleUserId());
            if (null != sysUser) {
                smsEquipment.setResponsibleUserName(sysUser.getName());
            }
            ResultData resultData = dicoBaseClient.findOrganizationById(smsEquipment.getResponsibleOrganizationId());
            if (null != resultData && resultData.getCode() == 0) {
                OrganizationFeignDomain organizationFeignDomain = (OrganizationFeignDomain) resultData.getData();
                smsEquipment.setResponsibleOrganizationName(organizationFeignDomain.getName());
            }
            // 设备保养记录
            List<SmsUserMaintenanceListDto> smsUserMaintenanceListDtoList = smsUserMaintenancePlanService.findUserMaintenanceRecordList(smsEquipment.getId());
            smsEquipment.setSmsUserMaintenanceListDtoList(smsUserMaintenanceListDtoList);
            List<SmsEquipmentFaultInfoDto> smsEquipmentFaultInfoDtoList = smsEquipmentFaultInfoService.findByStatusAndEquipmentId(true, smsEquipment.getId());
            for (SmsEquipmentFaultInfoDto smsEquipmentFaultInfoDto : smsEquipmentFaultInfoDtoList) {
                ResultData filesResultData = dicoBaseClient.findAttachmentListByTargetId(smsEquipmentFaultInfoDto.getId());
                if(filesResultData.getCode() == 0){
                    smsEquipmentFaultInfoDto.setFiles(filesResultData.getData());
                }
            }
            smsEquipment.setSmsEquipmentFaultInfoDtoList(smsEquipmentFaultInfoDtoList);
            return smsEquipment;
        }else {
            return null;
        }

    }

}
