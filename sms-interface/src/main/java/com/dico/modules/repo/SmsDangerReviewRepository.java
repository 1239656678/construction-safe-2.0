package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.SmsDangerReview;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface SmsDangerReviewRepository extends JpaDao<SmsDangerReview, String> {

    /**
     * 根据ID查询未删除的数据
     */
    SmsDangerReview getSmsDangerReviewByIdAndDelFlagIsFalse(String smsDangerReviewId);
}
