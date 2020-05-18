package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.Organization;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface OrganizationRepository extends JpaDao<Organization, String> {

    void deleteByIdIn(String[] ids);

    /**
     * 根据名称查询组织机构信息
     *
     * @author Gaodl
     * 方法名称: getOrganizationByNameEqualsAndDelFlagIsFalse
     * 参数： [organizationName]
     * 返回值： com.dico.modules.domain.Organization
     * 创建时间: 2019/2/28
     */
    Organization getOrganizationByNameEqualsAndDelFlagIsFalse(String organizationName);

    /**
     * 根据ID查询未删除的组织机构信息
     *
     * @author Gaodl
     * 方法名称: getOrganizationByIdAndDelFlagIsFalse
     * 参数： [organizationId]
     * 返回值： com.dico.modules.domain.Organization
     * 创建时间: 2019/2/28
     */
    Organization getOrganizationByIdAndDelFlagIsFalse(String organizationId);

    /**
     * 查询根组织
     *
     * @return
     */
    List<Organization> findByParentOrganizationIdIsNullAndDelFlagIsFalse();

    /**
     * 根据父组织ID查询所有的子组织
     *
     * @author Gaodl
     * 方法名称: findByParentProjectEqualsAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： java.util.List<com.dico.modules.domain.Organization>
     * 创建时间: 2019/4/8
     */
    List<Organization> findByParentOrganizationIdEqualsAndDelFlagIsFalse(String parentOrganizationId);

    List<Organization> findByNameLikeAndDelFlagIsFalse(String name);
}
