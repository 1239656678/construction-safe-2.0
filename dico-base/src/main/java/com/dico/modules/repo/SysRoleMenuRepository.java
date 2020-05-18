package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SysMenu;
import com.dico.modules.domain.SysRoleMenu;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 角色关联资源持久层业务类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: UserRepository
 * 创建时间: 2018/12/12
 */
public interface SysRoleMenuRepository extends JpaDao<SysRoleMenu, String> {

    /**
     * 获取角色资源关联信息
     *
     * @author Gaodl
     * 方法名称: getSysMenuByRoleId
     * 参数： [roleId]
     * 返回值： java.util.List<com.dico.modules.domain.SysMenu>
     * 创建时间: 2019/1/7
     */
    List<SysRoleMenu> getByRoleIdAndDelFlagIsFalse(String roleId);

    void deleteByIdIn(String[] ids);

    /**
     * 获取角色关联的资源信息
     *
     * @author Gaodl
     * 方法名称: getMenuByRoleId
     * 参数： [roleId]
     * 返回值： java.util.List<com.dico.modules.domain.SysMenu>
     * 创建时间: 2019/1/7
     */
    @Query("select new SysMenu(sm.id, sm.name, sm.address, sm.icon, sm.level, sm.parent, sm.areaId, sm.sort, sm.createUser, sm.createDate, sm.updateUser, sm.updateDate, sm.delFlag) from SysMenu sm, SysRoleMenu srm where sm.id = srm.menuId and sm.delFlag is false and srm.delFlag is false and srm.roleId = :roleId ")
    List<SysMenu> getMenuByRoleId(String roleId);

    /**
     * 查询未删除的指定角色资源关联信息
     *
     * @author Gaodl
     * 方法名称: getByRoleIdAndMenuId
     * 参数： [userId, roleId]
     * 返回值： com.dico.modules.domain.SysRoleMenu
     * 创建时间: 2019/1/7
     */
    SysRoleMenu getByRoleIdAndMenuIdAndDelFlagIsFalse(String userId, String roleId);

    /**
     * 物理删除指定角色菜单关联信息
     *
     * @author Gaodl
     * 方法名称: deleteByRoleIdAndMenuId
     * 参数： [roleId, menuId]
     * 返回值： void
     * 创建时间: 2019/1/8
     */
    void deleteByRoleIdAndMenuId(String roleId, String menuId);
}