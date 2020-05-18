package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.Attachments;
import com.dico.modules.repo.AttachmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 附件server层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: AttachmentsServer
 * 创建时间: 2019/1/15
 */
@Service
public class AttachmentsServer extends JpaService<Attachments, String> {

    @Autowired
    private AttachmentsRepository attachmentsRepository;

    @Override
    protected JpaDao<Attachments, String> getDao() {
        return attachmentsRepository;
    }

    /**
     * 根据ID查询未删除的数据
     *
     * @author Gaodl
     * 方法名称: getByIdEqualsAndDelFlagIsFalse
     * 参数： [id]
     * 返回值： com.dico.modules.domain.Attachments
     * 创建时间: 2019/4/18
     */
    public Attachments getByIdEqualsAndDelFlagIsFalse(String id) {
        return attachmentsRepository.getByIdEqualsAndDelFlagIsFalse(id);
    }

    public void deleteByIdIn(String[] ids) {
        attachmentsRepository.deleteByIdIn(ids);
    }

    /**
     * 根据targetId查询所有附件
     *
     * @author Gaodl
     * 方法名称: findByTargetIdEqualsAndDelFlagIsFalse
     * 参数： [targetId]
     * 返回值： java.util.List<com.dico.modules.domain.Attachments>
     * 创建时间: 2019/5/17
     */
    public List<Attachments> findByTargetIdEqualsAndDelFlagIsFalse(String targetId) {
        return attachmentsRepository.findByTargetIdEqualsAndDelFlagIsFalse(targetId);
    }

    /**
     * 根据ID集合查询附件
     *
     * @param ids
     * @return
     */
    public List<Attachments> findByIdIn(String[] ids) {
        return attachmentsRepository.findByIdInAndDelFlagIsFalse(ids);
    }
}
