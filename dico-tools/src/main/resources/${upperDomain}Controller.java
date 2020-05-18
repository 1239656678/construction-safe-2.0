package com.dico.modules.controller;

import com.dico.modules.domain.${upperDomain};
import com.dico.modules.service.${upperDomain}Service;
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
import java.util.*;

@RestController
@Api(tags = "${upperDomain}模块", produces = "${upperDomain}模块Api")
public class ${upperDomain}Controller{

    @Autowired
    private ${upperDomain}Service ${domain}Service;

    /**
     * 分页查询方法
     */
    @ResponseBody
    @GetMapping("/${domain}/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public ResultData dataPage(@RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum,@RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize){
        Page<${upperDomain}> ${domain}Page;
        try{
            Map<String, Object> queryMap=new HashMap<>(2);
            queryMap.put("delFlag_EQ",false);
            Sort sort=new Sort(Sort.Direction.DESC,"createDate");
            ${domain}Page = ${domain}Service.findAll(queryMap,pageNum,pageSize,sort);
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(${domain}Page);
    }

    /**
     * 不分页查询方法
     */
    @ResponseBody
    @GetMapping("/${domain}/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request){
        List<Map<String, Object>> listDataMap;
        try{
            Map<String, Object> queryMap=new HashMap<>(2);
            queryMap.put("delFlag_EQ",false);
            Sort sort=new Sort(Sort.Direction.DESC,"createDate");
            listDataMap=${domain}Service.findAll(queryMap,sort,request);
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 获取数据详情
     */
    @ResponseBody
    @GetMapping("/${domain}/dataInfo")
    @ApiOperation(value = "获取数据详情", notes = "获取数据详情")
    public ResultData dataInfo(String ${domain}Id){
        ${upperDomain} ${domain} = ${domain}Service.getById(${domain}Id);
        if(null==${domain}){
            return ResultData.getFailResult("数据不存在");
        }
        return ResultData.getSuccessResult(${domain});
    }

    /**
     * 新增方法
     */
    @ResponseBody
    @PostMapping("/${domain}/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request,@RequestBody ${upperDomain} ${domain}){
        try{
            // 获取当前登录用户
            String userId=TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult=ValiDatedUtils.valiDatedBean(${domain});
            if(!validatedResult.isStatus()){
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            ${domain}.setCreateUser(userId);
            ${domain}.setCreateDate(new Date());
            ${domain}.setDelFlag(false);
            ${domain}Service.save(${domain});
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     */
    @ResponseBody
    @PutMapping("/${domain}/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request,@RequestBody ${upperDomain} ${domain}){
        try{
            // 获取当前登录用户
            String userId=TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            ${upperDomain} _${domain} = ${domain}Service.getById(${domain}.getId());
            if(null==_${domain}){
                return ResultData.getFailResult("该信息不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult=ValiDatedUtils.valiDatedBean(${domain});
            if(!validatedResult.isStatus()){
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            _${domain}.setUpdateUser(userId);
            _${domain}.setUpdateDate(new Date());
            TransmitUtils.sources2destination(${domain},_${domain});
            ${domain}Service.update(_${domain});
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     */
    @ResponseBody
    @DeleteMapping("/${domain}/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids){
        try{
            ${domain}Service.deleteByIdIn(ids.split(","));
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     */
    @ResponseBody
    @DeleteMapping("/${domain}/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request,String ids){
        try{
            String[] idStr=ids.split(",");
            // 获取当前登录用户
            String userId=TokenUtils.getUserIdByRequest(request);
            for(String id:idStr){
                // 根据ID查询数据
                ${upperDomain} ${domain}=${domain}Service.getById(id);
                if(null!=${domain}){
                    // 设置删除标识为真
                    ${domain}.setDelFlag(true);
                    ${domain}.setUpdateDate(new Date());
                    ${domain}.setUpdateUser(userId);
                    ${domain}Service.update(${domain});
                }
            }
        }catch(Exception e){
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}
