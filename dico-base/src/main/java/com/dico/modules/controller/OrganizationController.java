package com.dico.modules.controller;

import com.dico.modules.domain.Organization;
import com.dico.modules.domain.SysOrganizationUser;
import com.dico.modules.domain.SysUser;
import com.dico.modules.service.OrganizationService;
import com.dico.modules.service.SysOrganizationUserService;
import com.dico.modules.service.SysUserService;
import com.dico.result.ResultData;
import com.dico.result.ValidatedResult;
import com.dico.util.TokenUtils;
import com.dico.util.TransmitUtils;
import com.dico.util.ValiDatedUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 组织机构业务模块
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: OrganizationController
 * 创建时间: 2019/2/28
 */
@RestController
@Api(tags = "组织机构模块", produces = "组织机构模块Api")
public class OrganizationController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SysOrganizationUserService sysOrganizationUserService;

    @Value("${organization.wbdw-query-key}")
    private String WBDW_QUERY_KEY;

    /**
     * 分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataPage
     * 参数： [name, pageNum, pageSize]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/2/28
     */
    @ResponseBody
    @GetMapping("/organization/dataPage")
    @ApiOperation(value = "获取组织机构分页数据", notes = "获取组织机构分页数据")
    public ResultData dataPage(String name, @RequestParam(required = false, defaultValue = "0", value = "pageNum") int pageNum, @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
        Page<Organization> organizations;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            organizations = organizationService.findAll(queryMap, pageNum, pageSize, sort);
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult(organizations);
    }

    /**
     * 不分页查询方法
     *
     * @author Gaodl
     * 方法名称: dataList
     * 参数： [request, username, name]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/2/28
     */
    @ResponseBody
    @GetMapping("/organization/dataList")
    @ApiOperation(value = "获取组织机构数据", notes = "获取数据方法")
    public ResultData dataList(HttpServletRequest request, String name) {
        List<Map<String, Object>> listDataMap;
        try {
            Map<String, Object> queryMap = new HashMap<>(3);
            queryMap.put("delFlag_EQ", false);
            if (StringUtils.isNotBlank(name)) {
                queryMap.put("name_LIKE", name);
            }
            Sort sort = new Sort(Sort.Direction.DESC, "createDate");
            listDataMap = organizationService.findAll(queryMap, sort, request);
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
     * 参数： [request, sysUser]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/2/28
     */
    @ResponseBody
    @PostMapping("/organization/save")
    @ApiOperation(value = "组织机构保存", notes = "保存方法")
    public ResultData save(HttpServletRequest request, @RequestBody Organization organization) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            SysUser sysUser = sysUserService.findOne(userId);
            if (null == sysUser) {
                return ResultData.getFailResult("获取当前登录人失败");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(organization);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            // 判断组织机构名称是否存在
            if (null != organizationService.getOrganizationByNameEqualsAndDelFlagIsFalse(organization.getName())) {
                return ResultData.getFailResult("组织机构名称已经存在");
            }
            organization.setCreateDate(new Date());
            organization.setCreateUser(userId);
            organization.setCreateUserName(sysUser.getName());
            organization.setDelFlag(false);
            organizationService.save(organization);
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
     * 参数： [request, sysUser]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/2/28
     */
    @ResponseBody
    @PutMapping("/organization/update")
    @ApiOperation(value = "组织机构修改", notes = "修改方法")
    public ResultData update(HttpServletRequest request, @RequestBody Organization organization) {
        try {
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            SysUser sysUser = sysUserService.findOne(userId);
            if (null == sysUser) {
                return ResultData.getFailResult("获取当前登录用户失败");
            }
            // 根据ID查询数据
            Organization _organization = organizationService.getOrganizationByIdAndDelFlagIsFalse(organization.getId());
            if (null == _organization) {
                return ResultData.getFailResult("该组织机构不存在");
            }
            // 校验实体类，如果校验不通过，则返回错误信息
            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(organization);
            if (!validatedResult.isStatus()) {
                return ResultData.getFailResult(validatedResult.getMessage());
            }
            // 判断组织机构名称是否与其他组织机构的重复了
            Organization _organization1 = organizationService.getOrganizationByNameEqualsAndDelFlagIsFalse(organization.getName());
            if (null != _organization1 && !_organization1.getId().equals(organization.getId())) {
                return ResultData.getFailResult("组织机构名称已经存在");
            }
            TransmitUtils.sources2destination(organization, _organization);
            _organization.setUpdateDate(new Date());
            _organization.setUpdateUser(userId);
            _organization.setUpdateUserName(sysUser.getName());
            organizationService.update(_organization);
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
     * 创建时间: 2019/2/28
     */
    @ResponseBody
    @DeleteMapping("/organization/delete")
    @ApiOperation(value = "组织机构物理删除", notes = "物理删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData delete(String ids) {
        try {
            // 判断该组织下边是否有子组织
            String[] idStrs = ids.split(",");
            for (String id : idStrs) {
                List<Organization> organizationList = organizationService.findChildrenOrganization(id);
                if (null != organizationList && organizationList.size() > 0) {
                    return ResultData.getFailResult("组织下存在子组织，请先删除子组织");
                }
            }
            organizationService.deleteByIdIn(ids.split(","));
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
     * 创建时间: 2019/2/28
     */
    @ResponseBody
    @DeleteMapping("/organization/remove")
    @ApiOperation(value = "组织机构逻辑删除", notes = "逻辑删除方法,可执行批量删除,参数用\",\"隔开")
    public ResultData remove(HttpServletRequest request, String ids) {
        try {
            String[] idStr = ids.split(",");
            // 获取当前登录用户
            String userId = TokenUtils.getUserIdByRequest(request);
            SysUser sysUser = sysUserService.findOne(userId);
            if (null == sysUser) {
                return ResultData.getFailResult("获取当前登录用户失败");
            }
            for (String id : idStr) {

                // 判断该组织下边是否有子组织
                List<Organization> organizationList = organizationService.findChildrenOrganization(id);
                if (null != organizationList && organizationList.size() > 0) {
                    return ResultData.getFailResult("组织下存在子组织，请先删除子组织");
                }

                // 根据ID查询数据
                Organization organization = organizationService.findOne(id);
                if (null != organization) {
                    organization.setDelFlag(true);// 设置删除标识为真
                    organization.setUpdateDate(new Date());
                    organization.setUpdateUser(userId);
                    organization.setUpdateUserName(sysUser.getName());
                    organizationService.update(organization);
                }
            }
        } catch (Exception e) {
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 根据单位ID获取组织机构树形数据
     *
     * @author Gaodl
     * 方法名称: findTreeList
     * 参数： [request, ids]
     * 返回值： com.dico.result.ResultData
     * 创建时间: 2019/4/8
     */
    @ResponseBody
    @GetMapping("/organization/findTreeList")
    @ApiOperation(value = "根据单位ID获取组织机构树形数据", notes = "根据单位ID获取组织机构树形数据")
    public ResultData findTreeList(String name) {
        // 查询所有根组织机构
        List<Organization> organizationList = null;
        if(StringUtils.isNotBlank(name)){
            organizationList = organizationService.findByNameLike(name);
        }else{
            organizationList = organizationService.findByParentOrganizationIdIsNull();
        }
        if (null != organizationList) {
            // 递归查询所有的子组织
            organizationList = organizationService.findChildrenOrganization(organizationList);
        }
        return ResultData.getSuccessResult(organizationList);
    }


    /**
     * 获取组织下的所有用户
     *
     * @param organizationId
     * @return
     */
    @ResponseBody
    @GetMapping("/organization/findUserByOrganizationId")
    @ApiOperation(value = "获取组织下的所有用户", notes = "获取组织下的所有用户")
    public ResultData findUserByOrganizationId(String organizationId) {
        if (StringUtils.isBlank(organizationId)) {
            return ResultData.getFailResult("组织机构ID不存在");
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<SysUser> sysUserList = sysUserService.findByOrganizationIdAndDelFlagIsFalseOrderByCreateDateDesc(organizationId);
        for (SysUser sysUser : sysUserList) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("id", sysUser.getId());
            dataMap.put("name", sysUser.getName());
            dataList.add(dataMap);
        }
        return ResultData.getSuccessResult(dataList);
    }

    /**
     * 获取部门领导人
     *
     * @param organizationId
     * @return
     */
    @ResponseBody
    @GetMapping("/organization/findOrganizationUser")
    @ApiOperation(value = "获取部门领导人", notes = "获取部门领导人")
    public ResultData findOrganizationUser(String organizationId) {
        if (StringUtils.isBlank(organizationId)) {
            return ResultData.getFailResult("组织机构ID不存在");
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<SysOrganizationUser> sysOrganizationUserList = sysOrganizationUserService.findByOrganizationId(organizationId);
        for (SysOrganizationUser sysOrganizationUser : sysOrganizationUserList) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("id", sysOrganizationUser.getSysUser().getId());
            dataMap.put("name", sysOrganizationUser.getSysUser().getName());
            dataList.add(dataMap);
        }
        return ResultData.getSuccessResult(dataList);
    }

    /**
     * 绑定部门领导人
     *
     * @param organizationId
     * @return
     */
    @ResponseBody
    @PostMapping("/organization/bindOrganizationUser")
    @ApiOperation(value = "绑定部门领导人", notes = "绑定部门领导人")
    public ResultData bindOrganizationUser(HttpServletRequest request, String organizationId, String userIds) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        Organization organization = null;
        if (StringUtils.isBlank(organizationId)) {
            return ResultData.getFailResult("组织机构ID不存在");
        }else{
            organization = organizationService.getOrganizationByIdAndDelFlagIsFalse(organizationId);
            if(null == organization){
                return ResultData.getFailResult("组织机构不存在");
            }
        }
        String[] ids = userIds.split(",");
        List<SysUser> sysUserList = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            SysUser sysUser = sysUserService.getUserByIdAndDelFlagIsFalse(id);
            if(null == sysUser){
                return ResultData.getFailResult("ID为："+id+"的用户不存在");
            }
            sysUserList.add(sysUser);
        }
        // 绑定之前先移除旧数据
        List<SysOrganizationUser> oldSysOrganizationUserList = sysOrganizationUserService.findByOrganizationId(organizationId);
        for (SysOrganizationUser oldSysOrganizationUser : oldSysOrganizationUserList){
            oldSysOrganizationUser.setUpdateDate(new Date());
            oldSysOrganizationUser.setUpdateUser(currentUserId);
            oldSysOrganizationUser.setDelFlag(true);
        }
        sysOrganizationUserService.save(oldSysOrganizationUserList);
        List<SysOrganizationUser> sysOrganizationUserList = new ArrayList<>();
        for (SysUser sysUser : sysUserList){
            SysOrganizationUser sysOrganizationUser = new SysOrganizationUser();
            sysOrganizationUser.setOrganization(organization);
            sysOrganizationUser.setSysUser(sysUser);
            sysOrganizationUser.setCreateDate(new Date());
            sysOrganizationUser.setCreateUser(currentUserId);
            sysOrganizationUser.setDelFlag(false);
            sysOrganizationUserList.add(sysOrganizationUser);
        }
        sysOrganizationUserService.save(sysOrganizationUserList);
        return ResultData.getSuccessResult();
    }

    /**
     * 解绑部门领导人
     *
     * @param organizationId
     * @return
     */
    @ResponseBody
    @PostMapping("/organization/removeOrganizationUser")
    @ApiOperation(value = "解绑部门领导人", notes = "解绑部门领导人")
    public ResultData removeOrganizationUser(HttpServletRequest request, String organizationId, String userIds) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        String[] ids = userIds.split(",");
        List<SysOrganizationUser> sysOrganizationUserList = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            SysOrganizationUser sysOrganizationUser = sysOrganizationUserService.findByOrganizationIdAndUserId(organizationId, ids[i]);
            if(null != sysOrganizationUser){
                sysOrganizationUser.setUpdateDate(new Date());
                sysOrganizationUser.setUpdateUser(currentUserId);
                sysOrganizationUser.setDelFlag(true);
                sysOrganizationUserList.add(sysOrganizationUser);
            }
        }
        sysOrganizationUserService.save(sysOrganizationUserList);
        return ResultData.getSuccessResult();
    }

    /**
     * 查询维保单位
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/organization/findWbdw")
    @ApiOperation(value = "查询维保单位", notes = "查询维保单位")
    public ResultData findWbdw(HttpServletRequest request) {
        String currentUserId = TokenUtils.getUserIdByRequest(request);
        if(StringUtils.isNotBlank(currentUserId)){
            ResultData.getFailResult("还未登陆，请先登录");
        }
        List<Organization> organizationList = organizationService.findChildrenOrganization(WBDW_QUERY_KEY);
        return ResultData.getSuccessResult(organizationList);
    }


}
