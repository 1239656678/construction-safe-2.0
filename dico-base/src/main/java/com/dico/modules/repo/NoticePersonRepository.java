package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.NoticePerson;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 通知人员持久层业务
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: NoticeMessageRepository
 * 创建时间: 2019/1/8
 */
public interface NoticePersonRepository extends JpaDao<NoticePerson, String> {

    /**
     * 根据用户ID获取信息
     *
     * @author Gaodl
     * 方法名称: findByUserIdAndDelFlagIsFalseOrderByCreateDateDesc
     * 参数： [userId]
     * 返回值： com.dico.modules.domain.NoticePerson
     * 创建时间: 2019/5/13
     */
    List<NoticePerson> findByUserIdAndDelFlagIsFalseOrderByCreateDateDesc(String userId);

    void deleteByIdIn(String[] ids);

    /**
     * 根据用户ID获取信息
     *
     * @author Gaodl
     * 方法名称: findByUserIdAndDelFlagIsFalseOrderByCreateDateDesc
     * 参数： [userId]
     * 返回值： com.dico.modules.domain.NoticePerson
     * 创建时间: 2019/5/13
     */
    @Query(value = "SELECT nm.id AS 'messageId', nm.TITLE AS 'messageTitle', nm.CONTENT AS 'messageContent', np.CREATE_DATE AS 'messageDate' FROM notice_person np LEFT JOIN notice_message nm ON np.MESSAGE_ID = nm.id where np.USER_ID = '1' AND np.DEL_FLAG IS FALSE", nativeQuery = true)
    List<Map<String, Object>> findAllByUserIdAndDelFlagIsFalseOrderByCreateDateDesc(String userId);

    List<NoticePerson> findByMessageIdEqualsAndDelFlagIsFalse(String messageId);


    @Modifying
    // 更新语句需要加@Modifying注解
    @Transactional
    @Query("update NoticePerson np set np.delFlag = true where np.userId = :userId and np.messageId = :messageId")
    void deleteByUserIdAndMessageId(@Param(value = "userId") String userId, @Param(value = "messageId") String messageId);
}
