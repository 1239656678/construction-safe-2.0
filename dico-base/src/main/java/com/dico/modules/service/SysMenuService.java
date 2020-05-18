package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.CurrentSysMenu;
import com.dico.modules.domain.SysMenu;
import com.dico.modules.domain.SysUser;
import com.dico.modules.repo.SysMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * 菜单service层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysMenuService
 * 创建时间: 2018/12/25
 */
@Service
public class SysMenuService extends JpaService<SysMenu, String> {

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    protected JpaDao<SysMenu, String> getDao() {
        return sysMenuRepository;
    }

    /**
     * 分页查询的方法
     *
     * @author Gaodl
     * 方法名称: findAll
     * 参数： [queryMap, pageNum, pageSize, sort]
     * 返回值： org.springframework.data.domain.Page<com.dico.modules.domain.SysMenu>
     * 创建时间: 2018/12/25
     */
    @Override
    public Page<SysMenu> findAll(Map<String, Object> queryMap, int pageNum, int pageSize, Sort sort) {
        return super.findAll(queryMap, pageNum, pageSize, sort);
    }

    public void deleteByIdIn(String[] ids) {
        sysMenuRepository.deleteByIdIn(ids);
    }

    /**
     * 根据ID获取未删除的数据
     *
     * @author Gaodl
     * 方法名称: getByIdAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.SysMenu
     * 创建时间: 2019/1/8
     */
    public SysMenu getByIdAndDelFlagIsFalse(String id) {
        return sysMenuRepository.getByIdAndDelFlagIsFalse(id);
    }

    /**
     * 获取当前用户的所有菜单权限
     *
     * @param currentUser
     * @return List<SysMenu>
     */
    public List<CurrentSysMenu> findAll(SysUser currentUser) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT sm.* FROM sys_menu sm LEFT JOIN sys_role_menu srm ON srm.MENU_ID = sm.id LEFT JOIN sys_role sr ON srm.ROLE_ID = sr.id LEFT JOIN sys_user_role sur ON sur.ROLE_ID = sr.id")
                .append(" WHERE sur.USER_ID = '").append(currentUser.getId())
                .append("' AND sm.DEL_FLAG = '0' AND srm.DEL_FLAG = '0' AND sur.DEL_FLAG = '0' AND sm.parent is null order by sm.sort asc");
        Query query = entityManager.createNativeQuery(sb.toString(), CurrentSysMenu.class);
        List<CurrentSysMenu> sysMenuList = query.getResultList();
        return this.findAllChildren(sysMenuList, currentUser);
    }

    private List<CurrentSysMenu> findAllChildren(List<CurrentSysMenu> sysMenuList, SysUser currentUser) {
        for (CurrentSysMenu sysMenu : sysMenuList) {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT sm.* FROM sys_menu sm LEFT JOIN sys_role_menu srm ON srm.MENU_ID = sm.id LEFT JOIN sys_role sr ON srm.ROLE_ID = sr.id LEFT JOIN sys_user_role sur ON sur.ROLE_ID = sr.id")
                    .append(" WHERE sur.USER_ID = '").append(currentUser.getId())
                    .append("' AND sm.DEL_FLAG = '0' AND srm.DEL_FLAG = '0' AND sur.DEL_FLAG = '0' AND sm.parent = '").append(sysMenu.getId()).append("' order by sm.sort asc");
            Query query = entityManager.createNativeQuery(sb.toString(), CurrentSysMenu.class);
            List<CurrentSysMenu> childrenSysMenuList = query.getResultList();
            if (null != childrenSysMenuList && childrenSysMenuList.size() > 0) {
                this.findAllChildren(childrenSysMenuList, currentUser);
            }
            sysMenu.setChildren(childrenSysMenuList);
        }
        return sysMenuList;
    }

    /**
     * 根据区域ID获取菜单
     *
     * @param areaId
     * @return
     */
    public SysMenu getByAreaId(String areaId) {
        return sysMenuRepository.getByAreaIdAndDelFlagIsFalse(areaId);
    }
}
