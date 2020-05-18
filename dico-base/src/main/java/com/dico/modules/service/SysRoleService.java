package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SysRole;
import com.dico.modules.repo.SysRoleRepository;
import com.dico.modules.repo.SysUserRoleRepository;
import com.dico.modules.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 角色service层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: UserService
 * 创建时间: 2018/12/12
 */
@Service
public class SysRoleService extends JpaService<SysRole, String> {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Override
    protected JpaDao<SysRole, String> getDao() {
        return sysRoleRepository;
    }

    /**
     * 分页查询的方法
     *
     * @author Gaodl
     * 方法名称: findAll
     * 参数： [queryMap, pageNum, pageSize, sort]
     * 返回值： org.springframework.data.domain.Page<com.dico.modules.domain.SysRole>
     * 创建时间: 2018/12/25
     */
    @Override
    public Page<SysRole> findAll(Map<String, Object> queryMap, int pageNum, int pageSize, Sort sort) {
        return super.findAll(queryMap, pageNum, pageSize, sort);
    }

    public void deleteByIdIn(String[] ids) {
        sysRoleRepository.deleteByIdIn(ids);
    }

    /**
     * 根据编码获取角色信息
     *
     * @author Gaodl
     * 方法名称: findOne
     * 参数： [code]
     * 返回值： com.dico.modules.domain.SysRole
     * 创建时间: 2018/12/25
     */
    public SysRole getOneByCodeEqualsAndDelFlagIsFalse(String code) {
        return sysRoleRepository.getOneByCodeEqualsAndDelFlagIsFalse(code);
    }

    /**
     * 根据ID查询未删除的角色
     *
     * @author Gaodl
     * 方法名称: getRoleByIdAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.SysRole
     * 创建时间: 2018/12/25
     */
    public SysRole getByIdAndDelFlagIsFalse(String id) {
        return sysRoleRepository.getByIdAndDelFlagIsFalse(id);
    }

    /**
     * 获取用户绑定的角色信息
     *
     * @author Gaodl
     * 方法名称: findBindRoles
     * 参数： [userId]
     * 返回值： com.dico.modules.domain.SysRole
     * 创建时间: 2019/4/4
     */
    public List<SysRole> findBindRoles(String userId) {
        return sysUserRoleService.getRoleByUserId(userId);
    }

    /**
     * 判断用户是否为系统管理员
     *
     * @author Gaodl
     * 方法名称: isSuper
     * 参数： [userId]
     * 返回值： boolean
     * 创建时间: 2019/4/4
     */
    public boolean isSuper(String userId) {
        SysRole sysRole = sysUserRoleRepository.isSuper(userId);
        return null != sysRole;
    }
}
