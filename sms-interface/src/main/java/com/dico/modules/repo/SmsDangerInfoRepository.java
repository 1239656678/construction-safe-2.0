package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsDangerInfo;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsDangerInfoRepository extends JpaDao<SmsDangerInfo, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsDangerInfo getSmsDangerInfoByIdAndDelFlagIsFalse(String smsDangerInfoId);
}
