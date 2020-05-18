package com.dico.modules.repo;

import com.dico.jpa.dao.JpaDao;
import com.dico.modules.domain.${upperDomain};

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public interface ${upperDomain}Repository extends JpaDao<${upperDomain},String>{

    /**
     * 根据ID查询未删除的数据
     */
    ${upperDomain} getByIdAndDelFlagIsFalse(String ${domain}Id);

    void deleteByIdIn(String[]ids);
}
