package com.dico.common.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * 通过调用context.setSessionCreationEnabled(false)表示不创建会话，如果之后调用Subject.getSession()将抛出DisabledSessionException异常。
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: StatelessDefaultSubjectFactory
 * 创建时间: 2018/12/12
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        // 不创建session
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
