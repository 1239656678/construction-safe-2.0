package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.NoticeMessage;
import com.dico.modules.repo.NoticeMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通知消息service层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: NoticeMessageService
 * 创建时间: 2019/1/8
 */
@Service
public class NoticeMessageService extends JpaService<NoticeMessage, String> {

    @Autowired
    private NoticeMessageRepository noticeMessageRepository;

    @Override
    protected JpaDao<NoticeMessage, String> getDao() {
        return noticeMessageRepository;
    }

    public NoticeMessage getByIdEqualsAndDelFlagIsFalse(String messageId) {
        return noticeMessageRepository.getByIdEqualsAndDelFlagIsFalse(messageId);
    }

    public void deleteByIdIn(String[] ids) {
        noticeMessageRepository.deleteByIdIn(ids);
    }
}
