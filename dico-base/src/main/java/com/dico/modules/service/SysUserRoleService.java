package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SysRole;
import com.dico.modules.domain.SysUserRole;
import com.dico.modules.repo.SysUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * 用户角色service层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysUserRoleService
 * 创建时间: 2019/1/7
 */
@Service
public class SysUserRoleService extends JpaService<SysUserRole, String> {

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Override
    protected JpaDao<SysUserRole, String> getDao() {
        return sysUserRoleRepository;
    }

    public void deleteByUserId(String userId) {
        sysUserRoleRepository.deleteByUserId(userId);
    }

    /**
     * 查询用户绑定的角色信息
     *
     * @author Gaodl
     * 方法名称: getUserRoleByUserIdAndDelFlagIsFalse
     * 参数： [userId]
     * 返回值： java.util.List<com.dico.modules.domain.SysUserRole>
     * 创建时间: 2019/1/7
     */
    public List<SysUserRole> getByUserIdAndDelFlagIsFalse(String userId) {
        return sysUserRoleRepository.getByUserIdAndDelFlagIsFalse(userId);
    }

    public void deleteByIdIn(String[] ids) {
        sysUserRoleRepository.deleteByIdIn(ids);
    }

    /**
     * 查询用户绑定的角色信息
     *
     * @author Gaodl
     * 方法名称: getRoleByUserId
     * 参数： [userId]
     * 返回值： java.util.List<com.dico.modules.domain.SysRole>
     * 创建时间: 2019/1/7
     */
    public List<SysRole> getRoleByUserId(String userId) {
        return sysUserRoleRepository.getRoleByUserId(userId);
    }

    /**
     * 获取用户角色指定的关联信息
     *
     * @author Gaodl
     * 方法名称: getUserRoleByUserIdAndRoleId
     * 参数： [userId, roleId]
     * 返回值： void
     * 创建时间: 2019/1/7
     */
    public SysUserRole getByUserIdAndRoleId(String userId, String roleId) {
        return sysUserRoleRepository.getByUserIdAndRoleIdAndDelFlagIsFalse(userId, roleId);
    }

    /**
     * 删除用户角色关联信息
     *
     * @author Gaodl
     * 方法名称: deleteByUserIdAndRoleId
     * 参数： [userId, roleId]
     * 返回值： void
     * 创建时间: 2019/1/7
     */
    public void deleteByUserIdAndRoleId(String userId, String roleId) {
        sysUserRoleRepository.deleteByUserIdAndRoleId(userId, roleId);
    }
}
