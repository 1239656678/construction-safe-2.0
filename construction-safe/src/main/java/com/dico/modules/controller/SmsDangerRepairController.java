package com.dico.modules.controller;

import com.dico.feign.domain.OrganizationFeignDomain;
import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.SmsDangerRepair;
import com.dico.modules.service.SmsDangerRepairService;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.TokenUtils;
import com.dico.util.TransmitUtils;
import com.dico.util.ValiDatedUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@Api(tags = "隐患整改模块", produces = "隐患整改模块Api")
public class SmsDangerRepairController {

    @Autowired
    private SmsDangerRepairService smsDangerRepairService;

    @Autowired
    private DicoBaseClient dicoBaseClient;

    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsDangerRepair/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsDangerRepair> smsDangerRepairPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsDangerRepairPage = smsDangerRepairService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsDangerRepairPage);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsDangerRepair/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = smsDangerRepairService.findAll(queryMap, sort, request);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/smsDangerRepair/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsDangerRepairId) {
        SmsDangerRepair smsDangerRepair = smsDangerRepairService.getSmsDangerRepairByIdAndDelFlagIsFalse(smsDangerRepairId);
        if (null == smsDangerRepair) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsDangerRepair);
    }

    /**
     * 根据隐患ID获取整改单信息
     */
    @ResponseBody
    @GetMapping("/smsDangerRepair/getRepairByDangerId")
    @ApiOperation(value = "根据隐患ID获取整改单信息", notes = "根据隐患ID获取整改单信息")
    public ResultData getRepairByDangerId(String smsDangerId) {
        SmsDangerRepair smsDangerRepair = smsDangerRepairService.getByDangerId(smsDangerId);
        if (null == smsDangerRepair) {
            return ResultData.getFailResult("数据不存在");
        }
        SysUser sysUser = dicoBaseClient.findUserById(smsDangerRepair.getRepairUserId());
        if (null != sysUser) {
            smsDangerRepair.setRepairUserId(sysUser.getName());
        }
        ResultData resultData = dicoBaseClient.findOrganizationById(smsDangerRepair.getRepairOrganizationId());
        if (resultData.getCode() == 0) {
            Map<String, String> dataMap = (Map<String, String>) resultData.getData();
            smsDangerRepair.setRepairOrganizationId(dataMap.get("name"));
        }
        ResultData resultData1 = dicoBaseClient.findAttachmentListByTargetId(smsDangerRepair.getId());
        if (resultData1.getCode() == 0) {
            smsDangerRepair.setFiles(resultData1.getData());
        }
        return ResultData.getSuccessResult(smsDangerRepair);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/smsDangerRepair/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsDangerRepair smsDangerRepair) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsDangerRepair);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsDangerRepair.setCreateUser(userId);
            smsDangerRepair.setCreateDate(new Date());
            smsDangerRepair.setDelFlag(false);
            smsDangerRepairService.save(smsDangerRepair);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/smsDangerRepair/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsDangerRepair smsDangerRepair) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsDangerRepair _smsDangerRepair = smsDangerRepairService.getSmsDangerRepairByIdAndDelFlagIsFalse(smsDangerRepair.getId());
            if (null == _smsDangerRepair) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsDangerRepair);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsDangerRepair.setUpdateUser(userId);
            _smsDangerRepair.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsDangerRepair, _smsDangerRepair);
            smsDangerRepairService.update(_smsDangerRepair);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsDangerRepair/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsDangerRepairService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsDangerRepair/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsDangerRepair smsDangerRepair = smsDangerRepairService.findOne(id);
                if (null != smsDangerRepair) {
                    // 设置删除标识为真
                    smsDangerRepair.setDelFlag(true);
                    smsDangerRepair.setUpdateDate(new Date());
                    smsDangerRepair.setUpdateUser(userId);
                    smsDangerRepairService.update(smsDangerRepair);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}
