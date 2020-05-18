package com.dico.feign.hystrix;

import com.dico.feign.domain.Attachments;
import com.dico.feign.domain.SysMenu;
import com.dico.feign.domain.SysRole;
import com.dico.feign.domain.SysUser;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.result.ImageResult;
import com.dico.result.ResultData;
import feign.hystrix.FallbackFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
// spring动态代理设置为cglib代理才能够使用熔断机制
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DicoBaseHystrix implements FallbackFactory<DicoBaseClient> {
    @Override
    public DicoBaseClient create(Throwable throwable) {
        return new DicoBaseClient() {
            @Override
            public List<SysRole> getBindRoles(String userId) {
                return null;
            }

            @Override
            public boolean isSuper(String userId) {
                return false;
            }

            @Override
            public SysUser findUserById(String userId) {
                return null;
            }

            @Override
            public ResultData generatorAreaMenus(SysMenu sysMenu) {
                return ResultData.getFailResult("请求失败");
            }

            @Override
            public SysMenu getSysMenuByAreaId(String areaId) {
                throwable.printStackTrace();
                System.out.println("===Hystrix=====areaId"+areaId);
                return null;
            }

            @Override
            public SysMenu getSysMenuById(String id) {
                return null;
            }

            @Override
            public void saveMenuEntity(SysMenu sysMenu) {
            }

            @Override
            public ResultData findAttachmentListByTargetId(String targetId) {
                return ResultData.getFailResult(throwable.getMessage());
            }

            @Override
            public ResultData findOrganizationById(String organizationId) {
                return ResultData.getFailResult(throwable.getMessage());
            }

            @Override
            public SysMenu getSysMenuIsAreaRoot() {
                return null;
            }

            @Override
            public ImageResult generatorTextQrCode(String content, String viewMsg, int width, int height) {
                throwable.printStackTrace();
                return null;
            }

            @Override
            public Attachments getAttachment(String id) {
                return null;
            }
        };
    }
}
