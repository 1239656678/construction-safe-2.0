package com.dico.modules.controller;

import com.dico.feign.domain.OrganizationFeignDomain;
import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.SmsDangerRepair;
import com.dico.modules.domain.SmsDangerReview;
import com.dico.modules.service.SmsDangerReviewService;
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
import java.util.*;

@RestController
@Api(tags = "隐患复查模块", produces = "隐患复查模块Api")
public class SmsDangerReviewController {

    @Autowired
    private SmsDangerReviewService smsDangerReviewService;

    @Autowired
    private DicoBaseClient dicoBaseClient;

    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsDangerReview/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<SmsDangerReview> smsDangerReviewPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            smsDangerReviewPage = smsDangerReviewService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(smsDangerReviewPage);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/smsDangerReview/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(2);
            queryMap.put("delFlag_EQ", false);
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = smsDangerReviewService.findAll(queryMap, sort, request);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/smsDangerReview/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String smsDangerReviewId) {
        SmsDangerReview smsDangerReview = smsDangerReviewService.getSmsDangerReviewByIdAndDelFlagIsFalse(smsDangerReviewId);
        if (null == smsDangerReview) {
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(smsDangerReview);
    }

    /**
     * 根据隐患ID获取复查单信息
     */
    @ResponseBody
    @GetMapping("/smsDangerReview/getReviewByDangerId")
    @ApiOperation(value = "根据隐患ID获取复查单信息", notes = "根据隐患ID获取复查单信息")
    public ResultData getReviewByDangerId(String smsDangerId) {
        SmsDangerReview smsDangerReview = smsDangerReviewService.getByDangerId(smsDangerId);
        if (null == smsDangerReview) {
            return ResultData.getFailResult("数据不存在");
        }
        SysUser sysUser = dicoBaseClient.findUserById(smsDangerReview.getReviewUserId());
        if (null != sysUser) {
            smsDangerReview.setReviewUserId(sysUser.getName());
        }
        ResultData resultData = dicoBaseClient.findOrganizationById(smsDangerReview.getReviewOrganizationId());
        if (resultData.getCode() == 0) {
            Map<String, String> dataMap = (Map<String, String>) resultData.getData();
            smsDangerReview.setReviewOrganizationId(dataMap.get("name"));
        }
        ResultData resultData1 = dicoBaseClient.findAttachmentListByTargetId(smsDangerReview.getId());
        if (resultData1.getCode() == 0) {
            smsDangerReview.setFiles(resultData1.getData());
        }
        return ResultData.getSuccessResult(smsDangerReview);
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/smsDangerReview/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody SmsDangerReview smsDangerReview) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsDangerReview);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            smsDangerReview.setCreateUser(userId);
            smsDangerReview.setCreateDate(new Date());
            smsDangerReview.setDelFlag(false);
            smsDangerReviewService.save(smsDangerReview);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/smsDangerReview/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody SmsDangerReview smsDangerReview) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            SmsDangerReview _smsDangerReview = smsDangerReviewService.getSmsDangerReviewByIdAndDelFlagIsFalse(smsDangerReview.getId());
            if (null == _smsDangerReview) {
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(smsDangerReview);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _smsDangerReview.setUpdateUser(userId);
            _smsDangerReview.setUpdateDate(new Date());
            TransmitUtils.sources2destination(smsDangerReview, _smsDangerReview);
            smsDangerReviewService.update(_smsDangerReview);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/smsDangerReview/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            smsDangerReviewService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/smsDangerReview/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                SmsDangerReview smsDangerReview = smsDangerReviewService.findOne(id);
                if (null != smsDangerReview) {
                    // 设置删除标识为真
                    smsDangerReview.setDelFlag(true);
                    smsDangerReview.setUpdateDate(new Date());
                    smsDangerReview.setUpdateUser(userId);
                    smsDangerReviewService.update(smsDangerReview);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}
