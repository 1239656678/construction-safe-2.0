package com.dico.feign.hystrix;

import com.dico.feign.domain.Attachments;
import com.dico.feign.domain.SysRole;
import com.dico.feign.domain.SysUser;
import com.dico.feign.domain.SysUserMap;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.result.ResultData;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用户熔断处理类
 *
 * @author Gaodl
 * 方法名称:
 * 参数：
 * 返回值：
 * 创建时间: 2018/12/28
 */
@Component
// spring动态代理设置为cglib代理才能够使用熔断机制
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DicoBaseHystrix implements FallbackFactory<DicoBaseClient> {

    private static final String REQUEST_FAIL = "请求失败";

    private static final Logger LOGGER = LoggerFactory.getLogger(DicoBaseHystrix.class);

    @Override
    public DicoBaseClient create(Throwable throwable) {
        return new DicoBaseClient() {
            @Override
            public List<SysRole> getBindRoles(String userId) {
                LOGGER.error(throwable.getMessage());
                return null;
            }

            @Override
            public boolean isSuper(String userId) {
                LOGGER.error(throwable.getMessage());
                return false;
            }

            @Override
            public SysUser findUserById(String userId) {
                LOGGER.error(throwable.getMessage());
                return null;
            }

            @Override
            public List<SysUserMap> findUserByOrganizationId(String organizationId) {
                LOGGER.error(throwable.getMessage());
                return null;
            }

            @Override
            public List<Attachments> findAttachmentListByIds(String[] ids) {
                LOGGER.error(throwable.getMessage());
                return null;
            }

            @Override
            public ResultData findAttachmentListByTargetId(String targetId) {
                LOGGER.error(throwable.getMessage());
                return ResultData.getFailResult(throwable.getMessage());
            }

            @Override
            public ResultData updateAttchment(Attachments attachments) {
                LOGGER.error(throwable.getMessage());
                return ResultData.getFailResult(REQUEST_FAIL);
            }

            @Override
            public ResultData updatePass(String userId, String oldPassWord, String newPassWord) {
                return ResultData.getFailResult(throwable.getMessage());
            }

            @Override
            public Boolean isOrganizationUser() {
                return false;
            }

            @Override
            public Boolean isWxxz() {
                return false;
            }

            @Override
            public Boolean isWbdw() {
                return false;
            }

            @Override
            public Attachments getAttachment(String id) {
                return null;
            }
        };
    }
}
