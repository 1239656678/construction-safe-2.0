package com.dico.modules.controller;

import com.alibaba.fastjson.JSON;
import com.dico.Exception.ErrorUsedException;
import com.dico.enums.DangerStatusEnum;
import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.*;
import com.dico.modules.service.SmsDangerInfoService;
import com.dico.modules.service.SmsDangerRepairService;
import com.dico.modules.service.SmsDangerReviewService;
import com.dico.modules.service.SmsEquipmentService;
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
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RestController
@Api(tags = "隐患信息模块", produces = "隐患信息模块Api")
public class SmsDangerInfoController {

    @Autowired
    private SmsDangerInfoService smsDangerInfoService;

    @Autowired
    private SmsDangerRepairService smsDangerRepairService;

    @Autowired
    private SmsDangerReviewService smsDangerReviewService;

    @Autowired
    private DicoBaseClient dicoBaseClient;

    @Autowired
    private SmsEquipmentService smsEquipmentService;

    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsDangerInfo/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsDangerInfo> smsDangerInfoPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsDangerInfoPage = smsDangerInfoService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsDangerInfoPage);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsDangerInfo/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request, @RequestParam(name = "dangerStatus", defaultValue = "4") int dangerStatus, @RequestParam(required = false) String remark, @RequestParam(required = false) String dangerAddress) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(4);
            queryMap.put("delFlag_EQ", false);
            if (dangerStatus != 4) {
                queryMap.put("dangerStatus_EQ", dangerStatus);
            }
            if (StringUtils.isNotBlank(remark)) {
                queryMap.put("remark_LIKE", remark);
            }
            if (StringUtils.isNotBlank(dangerAddress)) {
                queryMap.put("dangerAddress_LIKE", dangerAddress);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = smsDangerInfoService.findAll(queryMap, sort, request);
            if (null != listDataMap && listDataMap.size() > 0) {
                for (Map<String, Object> stringObjectMap : listDataMap) {
                    System.out.println(JSON.toJSONString(stringObjectMap));
                    if (null != stringObjectMap.get("equipmentId")) {
                        SmsEquipment smsEquipment = smsEquipmentService.getSmsEquipmentByIdAndDelFlagIsFalse(stringObjectMap.get("equipmentId").toString());
                        if (null != smsEquipment) {
                            stringObjectMap.put("equipmentId", smsEquipment.getName());
                        }
                    }
                    if (null != stringObjectMap.get("createUser")) {
                        SysUser sysUser = dicoBaseClient.findUserById(stringObjectMap.get("createUser").toString());
                        if (null != sysUser) {
                            stringObjectMap.put("createUserName", sysUser.getName());
                        }
                    }
                    if (null != stringObjectMap.get("id")) {
                        ResultData resultData = dicoBaseClient.findAttachmentListByTargetId(stringObjectMap.get("id").toString());
                        if (null != resultData && resultData.getCode() == 0) {

                            stringObjectMap.put("files", resultData.getData());
                        }
                    }
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
    @GetMapping("/smsDangerInfo/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsDangerInfoId) {
        SmsDangerInfo smsDangerInfo = smsDangerInfoService.getSmsDangerInfoByIdAndDelFlagIsFalse(smsDangerInfoId);
        if (null == smsDangerInfo) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsDangerInfo);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/smsDangerInfo/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsDangerInfo smsDangerInfo) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsDangerInfo);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsDangerInfo.setCreateUser(userId);
            smsDangerInfo.setCreateDate(new Date());
            smsDangerInfo.setDelFlag(false);
            smsDangerInfoService.save(smsDangerInfo);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/smsDangerInfo/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsDangerInfo smsDangerInfo) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsDangerInfo _smsDangerInfo = smsDangerInfoService.getSmsDangerInfoByIdAndDelFlagIsFalse(smsDangerInfo.getId());
            if (null == _smsDangerInfo) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsDangerInfo);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsDangerInfo.setUpdateUser(userId);
            _smsDangerInfo.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsDangerInfo, _smsDangerInfo);
            smsDangerInfoService.update(_smsDangerInfo);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsDangerInfo/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsDangerInfoService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsDangerInfo/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsDangerInfo smsDangerInfo = smsDangerInfoService.findOne(id);
                if (null != smsDangerInfo) {
                    // 设置删除标识为真
                    smsDangerInfo.setDelFlag(true);
                    smsDangerInfo.setUpdateDate(new Date());
                    smsDangerInfo.setUpdateUser(userId);
                    smsDangerInfoService.update(smsDangerInfo);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 获取整改单详情
     */
    @ResponseBody
    @GetMapping("/smsDangerInfo/getRepairInfo")
    @ApiOperation(value = "获取整改单详情", notes = "获取整改单详情")
    public ResultData getRepairInfo(HttpServletRequest request, String dangerId) {
        SmsDangerInfo smsDangerInfo = smsDangerInfoService.getSmsDangerInfoByIdAndDelFlagIsFalse(dangerId);
        if (null == smsDangerInfo) {
            return ResultData.getFailResult("隐患信息不存在");
        }
        SmsDangerRepair smsDangerRepair = smsDangerRepairService.getByDangerId(dangerId);
        if (null == smsDangerRepair) {
            smsDangerRepair = new SmsDangerRepair();
        }
        smsDangerRepair.setDangerRemark(smsDangerInfo.getRemark());
        ResultData resultData = dicoBaseClient.findAttachmentListByTargetId(smsDangerInfo.getId());
        if (resultData.getCode() == 0) {
            smsDangerRepair.setFiles(resultData.getData());
        }
        return ResultData.getSuccessResult(smsDangerRepair);
    }

    /**
     * 指派整改单
     */
    @ResponseBody
    @PostMapping("/smsDangerInfo/assignRepair")
    @ApiOperation(value = "指派整改单", notes = "指派整改单")
    public ResultData assignRepair(HttpServletRequest request, @RequestBody SmsDangerRepair smsDangerRepair) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            SmsDangerRepair _smsDangerRepair = smsDangerRepairService.getSmsDangerRepairByIdAndDelFlagIsFalse(smsDangerRepair.getId());
            if (null == _smsDangerRepair) {
                smsDangerRepair.setDelFlag(false);
                smsDangerRepair.setCreateUser(userId);
                smsDangerRepair.setCreateDate(new Date());
                smsDangerRepair.setRepairStatus(false);
                _smsDangerRepair = smsDangerRepair;
                smsDangerRepairService.save(_smsDangerRepair);
            } else {
                TransmitUtils.sources2destination(smsDangerRepair, _smsDangerRepair);
                _smsDangerRepair.setUpdateUser(userId);
                _smsDangerRepair.setUpdateDate(new Date());
                smsDangerRepairService.update(_smsDangerRepair);
            }
            if(smsDangerRepair.isChangeDangerStatus()){
                SmsDangerInfo smsDangerInfo = smsDangerInfoService.getSmsDangerInfoByIdAndDelFlagIsFalse(_smsDangerRepair.getDangerId());
                smsDangerInfo.setDangerStatus(DangerStatusEnum.REPAIR.getKey());
                smsDangerInfoService.update(smsDangerInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 指派复查单
     */
    @ResponseBody
    @PostMapping("/smsDangerInfo/assignReview")
    @ApiOperation(value = "指派复查单", notes = "指派复查单")
    public ResultData assignReview(HttpServletRequest request, @RequestBody SmsDangerReview smsDangerReview) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            SmsDangerReview _smsDangerReview = smsDangerReviewService.getSmsDangerReviewByIdAndDelFlagIsFalse(smsDangerReview.getId());
            if (null == _smsDangerReview) {
                smsDangerReview.setDelFlag(false);
                smsDangerReview.setCreateUser(userId);
                smsDangerReview.setCreateDate(new Date());
                smsDangerReview.setReviewStatus(false);
                _smsDangerReview = smsDangerReview;
                smsDangerReviewService.save(_smsDangerReview);
            } else {
                TransmitUtils.sources2destination(smsDangerReview, _smsDangerReview);
                _smsDangerReview.setUpdateUser(userId);
                _smsDangerReview.setUpdateDate(new Date());
                smsDangerReviewService.update(_smsDangerReview);
            }
            if(smsDangerReview.isChangeDangerStatus()){
                SmsDangerInfo smsDangerInfo = smsDangerInfoService.getSmsDangerInfoByIdAndDelFlagIsFalse(_smsDangerReview.getDangerId());
                smsDangerInfo.setDangerStatus(DangerStatusEnum.REVIEW.getKey());
                smsDangerInfoService.update(smsDangerInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultData.getSuccessResult();
    }
}
