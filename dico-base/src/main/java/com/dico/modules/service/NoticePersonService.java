package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.NoticePerson;
import com.dico.modules.repo.NoticePersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 通知人员service层
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: NoticeMessageService
 * 创建时间: 2019/1/8
 */
@Service
public class NoticePersonService extends JpaService<NoticePerson, String> {

    @Autowired
    private NoticePersonRepository noticePersonRepository;

    @Override
    protected JpaDao<NoticePerson, String> getDao() {
        return noticePersonRepository;
    }

    /**
     * 根据用户ID获取数据
     *
     * @author Gaodl
     * 方法名称: findByUserIdAndDelFlagIsFalseOrderByCreateDateDesc
     * 参数： [userId]
     * 返回值： com.dico.modules.domain.NoticePerson
     * 创建时间: 2019/5/13
     */
    public List<NoticePerson> findByUserIdAndDelFlagIsFalseOrderByCreateDateDesc(String userId) {
        return noticePersonRepository.findByUserIdAndDelFlagIsFalseOrderByCreateDateDesc(userId);
    }

    public void deleteByIdIn(String[] ids) {
        noticePersonRepository.deleteByIdIn(ids);
    }

    /**
     * 根据用户ID获取所有消息
     *
     * @author Gaodl
     * 方法名称: findByUserIdAndDelFlagIsFalseOrderByCreateDateDesc
     * 参数： [userId]
     * 返回值： java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * 创建时间: 2019/5/17
     */
    public List<Map<String, Object>> findAllByUserIdAndDelFlagIsFalseOrderByCreateDateDesc(String userId) {
        return noticePersonRepository.findAllByUserIdAndDelFlagIsFalseOrderByCreateDateDesc(userId);
    }

    /**
     * @param messageId
     * @return
     * @author xipeifeng
     * @since 2019-5-13
     * 根据消息ID获取读取人
     */
    public List<NoticePerson> findByMessageId(String messageId) {
        return noticePersonRepository.findByMessageIdEqualsAndDelFlagIsFalse(messageId);
    }

    public void deleteByUserIdAndMessageId(String userId, String messageId) {
        noticePersonRepository.deleteByUserIdAndMessageId(userId, messageId);
    }
}
