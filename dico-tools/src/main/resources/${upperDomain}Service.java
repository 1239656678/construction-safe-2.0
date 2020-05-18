package com.dico.modules.service;

import com.dico.jpa.dao.JpaDao;
import com.dico.jpa.service.JpaService;
import com.dico.modules.domain.${upperDomain};
import com.dico.modules.repo.${upperDomain}Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ${upperDomain}Service extends JpaService<${upperDomain},String>{

    @Autowired
    private ${upperDomain}Repository ${domain}Repository;

    @Override
    protected JpaDao<${upperDomain},String>getDao(){
        return ${domain}Repository;
    }

    /**
    * 根据ID查询未删除的数据
    */
    public ${upperDomain} getById(String ${domain}Id){
        return ${domain}Repository.getByIdAndDelFlagIsFalse(${domain}Id);
    }

    public void deleteByIdIn(String[]ids){
        ${domain}Repository.deleteByIdIn(ids);
    }
}
