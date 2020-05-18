package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SysUser;

import java.util.List;

/**
 * 用户持久层业务类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: UserRepository
 * 创建时间: 2018/12/12
 */
public interface SysUserRepository extends JpaDao<SysUser, String> {

    /**
     * 根据用户名查询用户
     *
     * @author Gaodl
     * 方法名称: findByUserName
     * 参数： [username]
     * 返回值： com.dico.modules.domain.User
     * 创建时间: 2018/12/12
     */
    SysUser getUserByUsername(String username);

    void deleteByIdIn(String[] ids);

    /**
     * 获取所有未删除的用户
     *
     * @author Gaodl
     * 方法名称: getUserByDelFlagIsFalse
     * 参数： []
     * 返回值： java.util.List<com.dico.modules.domain.User>
     * 创建时间: 2018/12/20
     */
    List<SysUser> getUserByDelFlagIsFalse();

    SysUser getUserByIdAndDelFlagIsFalse(String id);

    /**
     * 根据部门ID获取所有用户
     *
     * @author Gaodl
     * 方法名称: findByOrganizationIdAndDelFlagIsFalseOrderByCreateDateDesc
     * 参数： [organizationId]
     * 返回值： java.util.List<com.dico.modules.domain.SysUser>
     * 创建时间: 2019/5/21
     */
    List<SysUser> findByOrganizationIdAndDelFlagIsFalseOrderByCreateDateDesc(String organizationId);
}