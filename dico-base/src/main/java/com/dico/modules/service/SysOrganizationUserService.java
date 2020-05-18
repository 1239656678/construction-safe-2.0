package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SysOrganizationUser;
import com.dico.modules.domain.SysRole;
import com.dico.modules.domain.SysUser;
import com.dico.modules.domain.SysUserRole;
import com.dico.modules.repo.SysOrganizationUserRepository;
import com.dico.modules.repo.SysUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * 部门领导人service层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysUserRoleService
 * 创建时间: 2019/1/7
 */
@Service
public class SysOrganizationUserService extends JpaService<SysOrganizationUser, String> {

    @Autowired
    private SysOrganizationUserRepository sysOrganizationUserRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaDao<SysOrganizationUser, String> getDao() {
        return sysOrganizationUserRepository;
    }

    public void deleteByIdIn(String[] ids) {
        sysOrganizationUserRepository.deleteByIdIn(ids);
    }

    public List<SysOrganizationUser> findByOrganizationId(String organizationId) {
        return sysOrganizationUserRepository.findByOrOrganizationIdAndDelFlagIsFalse(organizationId);
    }

    public SysOrganizationUser findByOrganizationIdAndUserId(String organizationId, String id) {
        return sysOrganizationUserRepository.findByOrOrganizationIdAndSysUserIdAndDelFlagIsFalse(organizationId, id);
    }

    public SysOrganizationUser findByUserId(String userId) {
        return sysOrganizationUserRepository.findBySysUserIdAndDelFlagIsFalse(userId);
    }
}
