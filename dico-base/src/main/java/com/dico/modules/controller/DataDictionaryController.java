package com.dico.modules.controller;

import com.dico.modules.domain.DataDictionary;
import com.dico.modules.domain.DataDictionaryType;
import com.dico.modules.service.DataDictionaryService;
import com.dico.modules.service.DataDictionaryTypeService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 数据字典业务模块
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: DataDictionaryController
 * 创建时间: 2018/12/27
 */
@RestController
@Api(tags = "数据字典模块", produces = "数据字典模块Api")
public class DataDictionaryController {

    @Autowired
    private DataDictionaryTypeService dataDictionaryTypeService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataPage
     * 参数： [typeId, name, pageNum, pageSize]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @GetMapping("/data/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据方法")
    public ResultData dataPage(String typeId, String name, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<DataDictionary> dataDictionaryPage;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(typeId)) {
                queryMap.put("typeId_EQ", typeId);
            }
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            dataDictionaryPage = dataDictionaryService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(dataDictionaryPage);
    }

    /**
     * 分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataPage
     * 参数： [name, pageNum, pageSize]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @GetMapping("/dataType/dataPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据方法")
    public ResultData dataPage(String name, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<DataDictionaryType> dataDictionaryTypePage;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            dataDictionaryTypePage = dataDictionaryTypeService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(dataDictionaryTypePage);
    }

    /**
     * 不分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataList
     * 参数： [request, typeId, name]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @GetMapping("/data/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request, String typeId, String name) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(typeId)) {
                queryMap.put("typeId_EQ", typeId);
            }
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = dataDictionaryService.findAll(queryMap, sort, request);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 不分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataList
     * 参数： [request, name]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @GetMapping("/dataType/dataList")
    @ApiOperation(value = "获取数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request, String name) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = dataDictionaryTypeService.findAll(queryMap, sort, request);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(listDataMap);
    }

    /**
     * 新增方法
     *
     * @author Gaodl
     * 方法名称: save
     * 参数： [request, dataDictionary]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @PostMapping("/data/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody DataDictionary dataDictionary) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(dataDictionary);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            // 判断同类型下是否存在相同数据
            DataDictionary _dataDictionary = dataDictionaryService.getByNameEqualsAndTypeIdEqualsAndDelFlagIsFalse(dataDictionary.getName(), dataDictionary.getTypeId());
            if (null != _dataDictionary) {
                return ResultData.getFailResult("数据字典名称已经存在");
            }
            dataDictionary.setCreateDate(new Date());
            dataDictionary.setCreateUser(userId);
            dataDictionaryService.save(dataDictionary);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 新增方法
     *
     * @author Gaodl
     * 方法名称: save
     * 参数： [request, dataDictionaryType]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @PostMapping("/dataType/save")
    @ApiOperation(value = "保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody DataDictionaryType dataDictionaryType) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(dataDictionaryType);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            // 判断是否存在相同数据
            DataDictionaryType _dataDictionaryType = dataDictionaryTypeService.getByNameEqualsAndDelFlagIsFalse(dataDictionaryType.getName());
            if (null != _dataDictionaryType) {
                return ResultData.getFailResult("数据字典类型已经存在");
            }
            dataDictionaryType.setCreateDate(new Date());
            dataDictionaryType.setCreateUser(userId);
            dataDictionaryTypeService.save(dataDictionaryType);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     *
     * @author Gaodl
     * 方法名称: update
     * 参数： [request, dataDictionary]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @PutMapping("/data/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody DataDictionary dataDictionary) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            DataDictionary _dataDictionary = dataDictionaryService.getByIdAndDelFlagIsFalse(dataDictionary.getId());
            if (null == _dataDictionary) {
                return ResultData.getFailResult("该数据不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(dataDictionary);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            // 转换实体
            TransmitUtils.sources2destination(dataDictionary, _dataDictionary);
            _dataDictionary.setUpdateDate(new Date());
            _dataDictionary.setUpdateUser(userId);
            dataDictionaryService.update(_dataDictionary);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 更新方法
     *
     * @author Gaodl
     * 方法名称: update
     * 参数： [request, dataDictionaryType]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @PutMapping("/dataType/update")
    @ApiOperation(value = "修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody DataDictionaryType dataDictionaryType) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            // 根据ID查询数据
            DataDictionaryType _dataDictionaryType = dataDictionaryTypeService.getByIdAndDelFlagIsFalse(dataDictionaryType.getId());
            if (null == _dataDictionaryType) {
                return ResultData.getFailResult("该数据不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(dataDictionaryType);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            // 转换实体
            TransmitUtils.sources2destination(dataDictionaryType, _dataDictionaryType);
            _dataDictionaryType.setUpdateDate(new Date());
            _dataDictionaryType.setUpdateUser(userId);
            dataDictionaryTypeService.update(_dataDictionaryType);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     *
     * @author Gaodl
     * 方法名称: delete
     * 参数： [ids]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @DeleteMapping("/data/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            dataDictionaryService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 物理删除
     *
     * @author Gaodl
     * 方法名称: deleteType
     * 参数： [ids]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @DeleteMapping("/dataType/delete")
    @ApiOperation(value = "物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData deleteType(String ids) {
        try {
            dataDictionaryTypeService.deleteByIdIn(ids.split(","));
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     *
     * @author Gaodl
     * 方法名称: remove
     * 参数： [request, ids]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @DeleteMapping("/data/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                DataDictionary dataDictionary = dataDictionaryService.findOne(id);
                if (null != dataDictionary) {
                    dataDictionary.setDelFlag(true);// 设置删除标识为真
                    dataDictionary.setUpdateUser(userId);
                    dataDictionaryService.update(dataDictionary);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 逻辑删除
     *
     * @author Gaodl
     * 方法名称: removeType
     * 参数： [request, ids]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2018/12/28
     */
    @ResponseBody
    @DeleteMapping("/dataType/remove")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData removeType(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            for (String id : idStr) {
                // 根据ID查询数据
                DataDictionaryType dataDictionaryType = dataDictionaryTypeService.findOne(id);
                if (null != dataDictionaryType) {
                    dataDictionaryType.setDelFlag(true);// 设置删除标识为真
                    dataDictionaryType.setUpdateUser(userId);
                    dataDictionaryTypeService.update(dataDictionaryType);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }
}
