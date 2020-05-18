package com.dico.modules.controller;

import com.dico.feign.domain.OrganizationFeignDomain;
import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.SmsEquipment;
import com.dico.modules.domain.SmsEquipmentClass;
import com.dico.modules.domain.SmsRegions;
import com.dico.modules.dto.SmsEquipmentFaultInfoDto;
import com.dico.modules.dto.SmsUserMaintenanceListDto;
import com.dico.modules.service.*;
import com.dico.result.ImageResult;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.JavaBeanUtils;
import com.dico.util.TokenUtils;
import com.dico.util.TransmitUtils;
import com.dico.util.ValiDatedUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 设备模块
 */
@RestController
@Api(tags = "设备模块", produces = "设备模块Api")
public class SmsEquipmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsEquipmentController.class);

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    @Autowired
    private SmsEquipmentClassService smsEquipmentClassService;

    @Autowired
    private SmsRegionsService smsRegionsService;

    @Autowired
    private DicoBaseClient dicoBaseClient;

    @Autowired
    private SmsUserMaintenancePlanService smsUserMaintenancePlanService;

    @Autowired
    private SmsEquipmentFaultInfoService smsEquipmentFaultInfoService;

    @Value("${diy.equipment.qr-code-content}")
    private String QR_CODE_CONTENT;

    @Value("${diy.equipment.view-methd}")
    private String VIEW_METHD;

    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsEquipment/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false) String regionsId, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsEquipment> smsEquipmentPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            if (StringUtils.isNotBlank(regionsId)) {
                SmsRegions smsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(regionsId);
                if (null == smsRegions) {
                    return ResultData.getFailResult("区域不存在");
                }
                List<String> smsRegionsIdList = smsRegionsService.findChildrensId(smsRegions);
                queryMap.put("installRegionsId_IN", smsRegionsIdList.toArray());
            }
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsEquipmentPage = smsEquipmentService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsEquipmentPage);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsEquipment/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request, @RequestParam(required = false) String regionsId, @RequestParam(required = false) String code, @RequestParam(required = false) String name, @RequestParam(required = false) String status) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(4);
            if (StringUtils.isNotBlank(regionsId)) {
                SmsRegions smsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(regionsId);
                if (null == smsRegions) {
                    return ResultData.getFailResult("区域不存在");
                }
                List<String> smsRegionsIdList = smsRegionsService.findChildrensId(smsRegions);
                queryMap.put("installRegionsId_IN", smsRegionsIdList.toArray());
            }
            if (StringUtils.isNotBlank(code)) {
                queryMap.put("code_LIKE", code);
            }
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            if (StringUtils.isNotBlank(status)) {
                queryMap.put("status_EQ", status);
            }
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = smsEquipmentService.findAll(queryMap, sort, request);
            if (null != listDataMap && listDataMap.size() > 0) {
                for (Map<String, Object> stringObjectMap : listDataMap) {
                    if (null != stringObjectMap.get("installRegionsId")) {
                        SmsRegions smsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(stringObjectMap.get("installRegionsId").toString());
                        if (null != smsRegions) {
                            stringObjectMap.put("installRegionsName", smsRegions.getName());
                        }
                    }
                    SmsEquipmentClass smsEquipmentClass = smsEquipmentClassService.getSmsEquipmentClassByIdAndDelFlagIsFalse(stringObjectMap.get("typeId").toString());
                    stringObjectMap.put("typeName", smsEquipmentClass.getClassName());
                    if (null != stringObjectMap.get("responsibleUserId")) {
                        SysUser sysUser = dicoBaseClient.findUserById(stringObjectMap.get("responsibleUserId").toString());
                        if (null != sysUser) {
                            stringObjectMap.put("responsibleUserName", sysUser.getName());
                        }
                    }
                    if (null != stringObjectMap.get("responsibleOrganizationId")) {
                        ResultData resultData = dicoBaseClient.findOrganizationById(stringObjectMap.get("responsibleOrganizationId").toString());
                        if (null != resultData && resultData.getCode() == 0) {
                            Map dataMap = (Map) resultData.getData();
                            stringObjectMap.put("responsibleOrganizationName", dataMap.get("name"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/smsEquipment/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsEquipmentId) {
        SmsEquipment smsEquipment = smsEquipmentService.getSmsEquipmentByIdAndDelFlagIsFalse(smsEquipmentId);
        if (null == smsEquipment) {
            return ResultData.getFailResult("数据不存在");
        }
        SmsRegions smsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(smsEquipment.getInstallRegionsId());
        smsEquipment.setInstallRegionsId(smsRegions.getName());
        SmsEquipmentClass smsEquipmentClass = smsEquipmentClassService.getSmsEquipmentClassByIdAndDelFlagIsFalse(smsEquipment.getTypeId());
        smsEquipment.setTypeName(smsEquipmentClass.getClassName());
        SysUser sysUser = dicoBaseClient.findUserById(smsEquipment.getResponsibleUserId());
        if (null != sysUser) {
            smsEquipment.setResponsibleUserName(sysUser.getName());
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
        return ResultData.getSuccessResult(smsEquipment);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/smsEquipment/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsEquipment smsEquipment) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEquipment);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsEquipment.setCreateUser(userId);
            smsEquipment.setCreateDate(new Date());
            smsEquipment.setDelFlag(false);
            smsEquipmentService.save(smsEquipment);// 开始生成二维码
            if (smsEquipment.isGeneratorQr()) {
                if (StringUtils.isBlank(smsEquipment.getInstallRegionsId())) {
                    smsEquipment.setDelFlag(true);
                    smsEquipmentService.update(smsEquipment);
                    return ResultData.getFailResult("生成二维码的设备必须选定区域");
                }
                String equipmentName = smsRegionsService.findRegionsName(smsEquipment.getInstallRegionsId());
                equipmentName += " " + smsEquipment.getCode() + " " + smsEquipment.getName();
                ImageResult imageResult = dicoBaseClient.generatorTextQrCode(QR_CODE_CONTENT + VIEW_METHD + smsEquipment.getId(), equipmentName, 800, 800);
                if (null != imageResult) {
                    LOGGER.info("-------------name:" + imageResult.getImageName() + "-------------path:" + imageResult.getImagePath() + "-------------suffix:" + imageResult.getImageSuffix());
                    String imagePath = imageResult.getImagePath().split("static")[1];
                    smsEquipment.setQrCode(imagePath);
                    smsEquipmentService.update(smsEquipment);
                } else {
                    smsEquipment.setDelFlag(true);
                    smsEquipmentService.update(smsEquipment);
                    return ResultData.getFailResult("生成二维码失败");
                }
            }
        } catch (Exception e) {
            smsEquipment.setDelFlag(true);
            smsEquipmentService.update(smsEquipment);
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/smsEquipment/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsEquipment smsEquipment) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsEquipment _smsEquipment = smsEquipmentService.getSmsEquipmentByIdAndDelFlagIsFalse(smsEquipment.getId());
            if (null == _smsEquipment) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsEquipment);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsEquipment.setUpdateUser(userId);
            _smsEquipment.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsEquipment, _smsEquipment);
            smsEquipmentService.update(_smsEquipment);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }


    /**
     * 绑定区域
     *
     * @param request
     * @param regionsId   区域ID
     * @param equipmentId 设备ID
     * @return
     */
    @ResponseBody
    @PutMapping("/smsEquipment/bindRegions/{regionsId}")
    @ApiOperation(value = "绑定区域", notes = "绑定区域")
    public ResultData bindRegions(HttpServletRequest request, @PathVariable String regionsId, @RequestParam String equipmentId) {
        try {
            SmsRegions smsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(regionsId);
            if (null == smsRegions) {
                return ResultData.getFailResult("区域不存在");
            }
            SmsEquipment smsEquipment = smsEquipmentService.getSmsEquipmentByIdAndDelFlagIsFalse(equipmentId);
            if (null == smsEquipment) {
                return ResultData.getFailResult("设备不存在");
            }
            smsEquipment.setInstallRegionsId(smsRegions.getId());
            smsEquipmentService.update(smsEquipment);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsEquipment/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsEquipmentService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsEquipment/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsEquipment smsEquipment = smsEquipmentService.findOne(id);
                if (null != smsEquipment) {
                    // 设置删除标识为真
                    smsEquipment.setDelFlag(true);
                    smsEquipment.setUpdateDate(new Date());
                    smsEquipment.setUpdateUser(userId);
                    smsEquipmentService.update(smsEquipment);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 批量生成设备二维码
     */
    @ResponseBody
    @PostMapping("/smsEquipment/generatorEquipmentsQr")
    @ApiOperation(value = "批量生成设备二维码", notes = "批量生成设备二维码")
    public ResultData generatorEquipmentsQr(HttpServletRequest request, String ids) {
        try {
            List<SmsEquipment> smsEquipmentList = smsEquipmentService.findByIds(ids.split(","));
            boolean flag = true;
            String equipmentId = "";
            for (SmsEquipment smsEquipment : smsEquipmentList) {
                if (StringUtils.isNotBlank(smsEquipment.getInstallRegionsId()) && StringUtils.isBlank(smsEquipment.getQrCode())) {
                    String equipmentName = smsRegionsService.findRegionsName(smsEquipment.getInstallRegionsId());
                    equipmentName += " " + smsEquipment.getCode() + " " + smsEquipment.getName();
                    ImageResult imageResult = dicoBaseClient.generatorTextQrCode(QR_CODE_CONTENT + VIEW_METHD + smsEquipment.getId(), equipmentName, 800, 800);
                    if (null == imageResult) {
                        flag = false;
                        equipmentId = smsEquipment.getId();
                        break;
                    }
                    LOGGER.info("-------------name:" + imageResult.getImageName() + "-------------path:" + imageResult.getImagePath() + "-------------suffix:" + imageResult.getImageSuffix());
                    String imagePath = imageResult.getImagePath().split("static")[1];
                    smsEquipment.setQrCode(imagePath);
                }
            }
            if (!flag) {
                return ResultData.getFailResult("二维码生成失败->" + equipmentId);
            } else {
                smsEquipmentService.save(smsEquipmentList);
                return ResultData.getSuccessResult("二维码生成成功");
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
    }

    /**
     * 查询未绑定区域的设备
     */
    @ResponseBody
    @GetMapping("/smsEquipment/findEquipmentByRegionsIsNull")
    @ApiOperation(value = "查询未绑定区域的设备", notes = "查询未绑定区域的设备")
    public ResultData findEquipmentByRegionsIsNull(HttpServletRequest request, @RequestParam(required = false) String code, @RequestParam(required = false) String name) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        List<SmsEquipment> smsEquipmentList = smsEquipmentService.findByInstallRegionsIdIsNull(code, name);
        for (SmsEquipment smsEquipment : smsEquipmentList) {
            SmsEquipmentClass smsEquipmentClass = smsEquipmentClassService.getSmsEquipmentClassByIdAndDelFlagIsFalse(smsEquipment.getTypeId());
            smsEquipment.setTypeName(smsEquipmentClass.getClassName());
            if (StringUtils.isNotBlank(smsEquipment.getResponsibleUserId())) {
                SysUser sysUser = dicoBaseClient.findUserById(smsEquipment.getResponsibleUserId());
                if (null != sysUser) {
                    smsEquipment.setResponsibleUserName(sysUser.getName());
                }
            }
        }
        return ResultData.getSuccessResult(smsEquipmentList);
    }

    /**
     * 根据设备分类ID删除设备
     * @param request
     * @param classId
     * @return
     */
    @ResponseBody
    @DeleteMapping("/smsEquipment/removeByClassId")
    @ApiOperation(value = "根据设备分类ID删除设备", notes = "根据设备分类ID删除设备")
    public ResultData removeByClassId(HttpServletRequest request, @RequestParam String classId) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        List<SmsEquipmentClass> smsEquipmentClassList = smsEquipmentClassService.findChildrens(classId);
        List<String> idList = new ArrayList<>();
        this.getId(smsEquipmentClassList, idList);
        String[] ids = new String[idList.size()];
        idList.toArray(ids);
        List<SmsEquipment> smsEquipmentList = smsEquipmentService.findByTypeIdIn(ids);
        for (SmsEquipment smsEquipment : smsEquipmentList) {
            smsEquipment.setUpdateDate(new Date());
            smsEquipment.setUpdateUser(currentUserId);
            smsEquipment.setDelFlag(true);
        }
        smsEquipmentService.save(smsEquipmentList);
        return ResultData.getSuccessResult();
    }

    /**
     * 根据设备分类ID查询设备
     * @param request
     * @param classId
     * @return
     */
    @ResponseBody
    @GetMapping("/smsEquipment/findByClassId")
    @ApiOperation(value = "根据设备分类ID查询设备", notes = "根据设备分类ID查询设备")
    public ResultData findByClassId(HttpServletRequest request, @RequestParam String classId) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        List<SmsEquipmentClass> smsEquipmentClassList = smsEquipmentClassService.findChildrens(classId);
        List<String> idList = new ArrayList<>();
        this.getId(smsEquipmentClassList, idList);
        String[] ids = new String[idList.size()];
        idList.toArray(ids);
        List<SmsEquipment> smsEquipmentList=smsEquipmentService.findByTypeIdIn(ids);
        for(SmsEquipment smsEquipment:smsEquipmentList){
            SmsRegions smsRegions = smsRegionsService.getSmsRegionsByIdAndDelFlagIsFalse(smsEquipment.getInstallRegionsId());
            if (null != smsRegions) {
                smsEquipment.setInstallRegionsName(smsRegions.getName());
            }
        }
        return ResultData.getSuccessResult(smsEquipmentList);
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
}
