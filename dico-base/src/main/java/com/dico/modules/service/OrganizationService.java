package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.Organization;
import com.dico.modules.repo.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 组织机构service层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: OrganizationService
 * 创建时间: 2019/2/27
 */
@Service
public class OrganizationService extends JpaService<Organization, String> {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    protected JpaDao<Organization, String> getDao() {
        return organizationRepository;
    }

    public void deleteByIdIn(String[] ids) {
        organizationRepository.deleteByIdIn(ids);
    }

    /**
     * 根据名称查询组织机构信息
     *
     * @author Gaodl
     * 方法名称: getOrganizationByNameEqualsAndDelFlagIsFalse
     * 参数： [name]
     * 返回值： java.lang.Object
     * 创建时间: 2019/2/28
     */
    public Organization getOrganizationByNameEqualsAndDelFlagIsFalse(String organizationName) {
        return organizationRepository.getOrganizationByNameEqualsAndDelFlagIsFalse(organizationName);
    }

    /**
     * 根据ID查询未删除的组织机构信息
     *
     * @author Gaodl
     * 方法名称: getOrganizationByIdAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.Organization
     * 创建时间: 2019/2/28
     */
    public Organization getOrganizationByIdAndDelFlagIsFalse(String organizationId) {
        return organizationRepository.getOrganizationByIdAndDelFlagIsFalse(organizationId);
    }

    /**
     * 查询根组织
     *
     * @return
     */
    public List<Organization> findByParentOrganizationIdIsNull() {
        return organizationRepository.findByParentOrganizationIdIsNullAndDelFlagIsFalse();
    }

    /**
     * 递归查询所有的子组织
     *
     * @author Gaodl
     * 方法名称: findChildrenOrganization
     * 参数： [organizationList]
     * 返回值： java.util.List<com.dico.modules.domain.Organization>
     * 创建时间: 2019/4/8
     */
    public List<Organization> findChildrenOrganization(List<Organization> organizationList) {
        for (Organization organization : organizationList) {
            List<Organization> organizationChildrenList = organizationRepository.findByParentOrganizationIdEqualsAndDelFlagIsFalse(organization.getId());
            organization.setChildren(organizationChildrenList);
            this.findChildrenOrganization(organizationChildrenList);
        }
        return organizationList;
    }

    /**
     * 根据组织ID查询所有的子组织
     *
     * @author Gaodl
     * 方法名称: findChildrenOrganization
     * 参数： [id]
     * 返回值： java.util.List<com.dico.modules.domain.Organization>
     * 创建时间: 2019/4/9
     */
    public List<Organization> findChildrenOrganization(String id) {
        return organizationRepository.findByParentOrganizationIdEqualsAndDelFlagIsFalse(id);
    }

    public List<Organization> findByNameLike(String name) {
        return organizationRepository.findByNameLikeAndDelFlagIsFalse("%"+name+"%");
    }
}
