package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SysRole;

/**
 * 角色持久层业务类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: UserRepository
 * 创建时间: 2018/12/12
 */
public interface SysRoleRepository extends JpaDao<SysRole, String> {

    /**
     * 根据编码查询未删除的角色
     *
     * @author Gaodl
     * 方法名称: getOneByCodeEqualsAndDelFlagIsFalse
     * 参数： [code]
     * 返回值： com.dico.modules.domain.SysRole
     * 创建时间: 2018/12/25
     */
    SysRole getOneByCodeEqualsAndDelFlagIsFalse(String code);

    void deleteByIdIn(String[] ids);

    /**
     * 根据ID查询未删除的角色
     *
     * @author Gaodl
     * 方法名称: getRoleByIdAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.SysRole
     * 创建时间: 2018/12/25
     */
    SysRole getByIdAndDelFlagIsFalse(String id);
}