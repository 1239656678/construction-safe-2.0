package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.NoticeMessage;

/**
 * 通知消息持久层业务
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: NoticeMessageRepository
 * 创建时间: 2019/1/8
 */
public interface NoticeMessageRepository extends JpaDao<NoticeMessage, String> {

    NoticeMessage getByIdEqualsAndDelFlagIsFalse(String messageId);

    void deleteByIdIn(String[] ids);
}
