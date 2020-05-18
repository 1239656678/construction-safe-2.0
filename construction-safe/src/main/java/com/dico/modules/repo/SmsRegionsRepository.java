package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsRegions;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsRegionsRepository extends JpaDao<SmsRegions, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsRegions getSmsRegionsByIdAndDelFlagIsFalse(String smsRegionsId);
    /**
     * 根据Name查询未删除的数据
     */
    List<SmsRegions> getSmsRegionsByNameAndDelFlagIsFalse(String name);

    void deleteByIdIn(String[] ids);

    /**
     * 查询所有根区域
     *
     * @return
     */
    List<SmsRegions> findByPidIsNullAndDelFlagIsFalse();

    /**
     * 根据上级区域ID查询下级区域
     *
     * @param pid
     * @return
     */
    List<SmsRegions> findByPidAndDelFlagIsFalse(String pid);

}
