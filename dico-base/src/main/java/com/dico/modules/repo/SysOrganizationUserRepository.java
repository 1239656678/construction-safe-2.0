package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SysOrganizationUser;
import com.dico.modules.domain.SysRole;
import com.dico.modules.domain.SysUserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 部门领导人持久层业务类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysUserRoleRepository
 * 创建时间: 2019/1/7
 */
public interface SysOrganizationUserRepository extends JpaDao<SysOrganizationUser, String> {

    void deleteByIdIn(String[] ids);

    List<SysOrganizationUser> findByOrOrganizationIdAndDelFlagIsFalse(String organizationId);

    SysOrganizationUser findByOrOrganizationIdAndSysUserIdAndDelFlagIsFalse(String organizationId, String id);

    SysOrganizationUser findBySysUserIdAndDelFlagIsFalse(String userId);
}