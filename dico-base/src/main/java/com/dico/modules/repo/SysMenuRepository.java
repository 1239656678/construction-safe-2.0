package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SysMenu;

/**
 * 菜单持久层业务类
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: UserRepository
 * 创建时间: 2018/12/12
 */
public interface SysMenuRepository extends JpaDao<com.dico.modules.domain.SysMenu, String> {

    /**
     * 根据ID查询未删除的菜单
     *
     * @author Gaodl
     * 方法名称: getSysMenuByIdEqualsAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.SysMenu
     * 创建时间: 2018/12/25
     */
    SysMenu getByIdAndDelFlagIsFalse(String id);

    void deleteByIdIn(String[] ids);

    /**
     * 根据区域ID获取菜单
     *
     * @param areaId
     * @return
     */
    SysMenu getByAreaIdAndDelFlagIsFalse(String areaId);
}