package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SysRole;
import com.dico.modules.domain.SysUserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 用户角色持久层业务类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysUserRoleRepository
 * 创建时间: 2019/1/7
 */
public interface SysUserRoleRepository extends JpaDao<SysUserRole, String> {

    /**
     * 删除用户下所有关联的角色
     *
     * @author Gaodl
     * 方法名称: deleteByUserId
     * 参数： [userId]
     * 返回值： void
     * 创建时间: 2019/1/7
     */
    void deleteByUserId(String userId);

    void deleteByIdIn(String[] ids);

    /**
     * 删除用户下所有关联的角色
     *
     * @author Gaodl
     * 方法名称: removeByUserId
     * 参数： [userId]
     * 返回值： void
     * 创建时间: 2019/1/7
     */
    void removeByUserId(String userId);

    /**
     * 查询用户绑定的角色信息
     *
     * @author Gaodl
     * 方法名称: getUserRoleByUserIdAndDelFlagIsFalse
     * 参数： [userId]
     * 返回值： java.util.List<com.dico.modules.domain.SysUserRole>
     * 创建时间: 2019/1/7
     */
    List<SysUserRole> getByUserIdAndDelFlagIsFalse(String userId);

    /**
     * 查询用户绑定的角色信息
     *
     * @author Gaodl
     * 方法名称: getRoleByUserId
     * 参数： [userId]
     * 返回值： java.util.List<com.dico.modules.domain.SysRole>
     * 创建时间: 2019/1/7
     */
    @Query("select new SysRole(sr.id, sr.code, sr.name, sr.createUser, sr.createDate, sr.updateUser, sr.updateDate, sr.delFlag) from SysRole sr, SysUserRole sur where sr.id = sur.roleId and sr.delFlag is false and sur.delFlag is false and sur.userId = :userId")
    List<SysRole> getRoleByUserId(@Param("userId") String userId);

    /**
     * 判断用户是否为系统管理员
     *
     * @author Gaodl
     * 方法名称: isSuper
     * 参数： [userId]
     * 返回值： com.dico.modules.domain.SysRole
     * 创建时间: 2019/4/4
     */
    @Query("select new SysRole(sr.id, sr.code, sr.name, sr.createUser, sr.createDate, sr.updateUser, sr.updateDate, sr.delFlag) from SysRole sr, SysUserRole sur where sr.id = sur.roleId and sr.delFlag is false and sr.code = 'super' and sur.delFlag is false and sur.userId = :userId")
    SysRole isSuper(@Param("userId") String userId);

    /**
     * 获取用户角色指定的关联信息
     *
     * @author Gaodl
     * 方法名称: getUserRoleByUserIdAndRoleId
     * 参数： [userId, roleId]
     * 返回值： com.dico.modules.domain.SysUserRole
     * 创建时间: 2019/1/7
     */
    SysUserRole getByUserIdAndRoleIdAndDelFlagIsFalse(String userId, String roleId);

    /**
     * 删除用户角色关联信息
     *
     * @author Gaodl
     * 方法名称: deleteByUserIdAndRoleId
     * 参数： [userId, roleId]
     * 返回值： void
     * 创建时间: 2019/1/7
     */
    void deleteByUserIdAndRoleId(String userId, String roleId);
}