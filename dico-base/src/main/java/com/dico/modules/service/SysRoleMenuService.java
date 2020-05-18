package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.SysMenu;
import com.dico.modules.domain.SysRoleMenu;
import com.dico.modules.repo.SysRoleMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色关联资源service层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: UserService
 * 创建时间: 2018/12/12
 */
@Service
public class SysRoleMenuService extends JpaService<SysRoleMenu, String> {

    @Autowired
    private SysRoleMenuRepository sysRoleMenuRepository;

    @Override
    protected JpaDao<SysRoleMenu, String> getDao() {
        return sysRoleMenuRepository;
    }


    /**
     * 获取角色资源关联信息
     *
     * @author Gaodl
     * 方法名称: getByRoleIdAndDelFlagIsFalse
     * 参数： [roleId]
     * 返回值： java.util.List<com.dico.modules.domain.SysRoleMenu>
     * 创建时间: 2019/1/7
     */
    public List<SysRoleMenu> getByRoleIdAndDelFlagIsFalse(String roleId) {
        return sysRoleMenuRepository.getByRoleIdAndDelFlagIsFalse(roleId);
    }

    public void deleteByIdIn(String[] ids) {
        sysRoleMenuRepository.deleteByIdIn(ids);
    }

    /**
     * @author Gaodl
     * 方法名称: getMenuByRoleId
     * 参数： [roleId]
     * 返回值： java.util.List<com.dico.modules.domain.SysMenu>
     * 创建时间: 2019/1/7
     */
    public List<SysMenu> getMenuByRoleId(String roleId) {
        return sysRoleMenuRepository.getMenuByRoleId(roleId);
    }

    /**
     * 查询未删除的指定角色资源关联信息
     *
     * @author Gaodl
     * 方法名称: getByRoleIdAndMenuId
     * 参数： [userId, roleId]
     * 返回值： com.dico.modules.domain.SysRoleMenu
     * 创建时间: 2019/1/7
     */
    public SysRoleMenu getByRoleIdAndMenuIdAndDelFlagIsFalse(String userId, String roleId) {
        return sysRoleMenuRepository.getByRoleIdAndMenuIdAndDelFlagIsFalse(userId, roleId);
    }

    /**
     * 物理删除指定角色菜单关联信息
     *
     * @author Gaodl
     * 方法名称: deleteByRoleIdAndMenuId
     * 参数： [roleId, menuId]
     * 返回值： void
     * 创建时间: 2019/1/8
     */
    public void deleteByRoleIdAndMenuId(String roleId, String menuId) {
        sysRoleMenuRepository.deleteByRoleIdAndMenuId(roleId, menuId);
    }
}
