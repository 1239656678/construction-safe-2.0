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
    SmsRegions getByIdAndDelFlagIsFalse(String smsRegionsId);

    /**
     * 根据父ID查询区域
     *
     * @param id
     * @return
     */
    List<SmsRegions> findByPidAndDelFlagIsFalse(String id);
}
