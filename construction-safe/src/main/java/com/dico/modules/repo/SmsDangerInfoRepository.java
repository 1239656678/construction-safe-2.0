package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsDangerInfo;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

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

    void deleteByIdIn(String[] ids);

    @Query("select count(sdi.id) from SmsDangerInfo sdi where sdi.delFlag is false")
    int findCount();

    @Query("select count(sdi.id) from SmsDangerInfo sdi where sdi.createDate between ?1 and ?2 and sdi.delFlag is false")
    int findCount(Date preDate, Date nextDate);

    @Query("select count(sdi.id) from SmsDangerInfo sdi where sdi.delFlag is false and sdi.createDate between :firstDate and :lastDate")
    int findCountByCreateDateBetween(Date firstDate, Date lastDate);
}
