package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.Attachments;

import java.util.List;

/**
 * 附件持久层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: AttachmentsRepository
 * 创建时间: 2019/1/15
 */
public interface AttachmentsRepository extends JpaDao<Attachments, String> {
    /**
     * 根据ID查询未删除的数据
     *
     * @author Gaodl
     * 方法名称: getByIdEqualsAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.Attachments
     * 创建时间: 2019/4/18
     */
    Attachments getByIdEqualsAndDelFlagIsFalse(String id);

    void deleteByIdIn(String[] ids);

    /**
     * 根据targetId查询所有附件
     *
     * @author Gaodl
     * 方法名称: findByTargetIdEqualsAndDelFlagIsFalse
     * 参数： [targetId]
     * 返回值： java.util.List<com.dico.modules.domain.Attachments>
     * 创建时间: 2019/5/17
     */
    List<Attachments> findByTargetIdEqualsAndDelFlagIsFalse(String targetId);

    /**
     * 根据ID集合查询附件
     *
     * @param ids
     * @return
     */
    List<Attachments> findByIdInAndDelFlagIsFalse(String[] ids);
}
