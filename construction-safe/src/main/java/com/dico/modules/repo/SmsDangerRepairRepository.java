package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsDangerRepair;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsDangerRepairRepository extends JpaDao<SmsDangerRepair, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsDangerRepair getSmsDangerRepairByIdAndDelFlagIsFalse(String smsDangerRepairId);

    void deleteByIdIn(String[] ids);

    SmsDangerRepair getByDangerIdAndDelFlagIsFalse(String dangerId);

    @Query("select count(sdr.id) from SmsDangerRepair sdr where sdr.delFlag is false")
    int findCount();

    @Query("select count(sdr.id) from SmsDangerRepair sdr where sdr.delFlag is false and sdr.updateDate between :firstDate and :lastDate")
    int findCountByUpdateDateBetween(Date firstDate, Date lastDate);

    @Query("SELECT count(sdr.id) FROM SmsDangerRepair sdr LEFT JOIN SmsDangerInfo sdi ON sdi.id = sdr.dangerId AND sdi.delFlag IS FALSE WHERE sdr.delFlag IS FALSE AND sdr.repairStatus IS TRUE AND sdi.createDate between ?1 and ?2")
    int findCount(Date firstDate, Date lastDate);
}
