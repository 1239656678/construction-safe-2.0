package com.dico.feign.hystrix;

import com.dico.feign.feignClient.ConstructionSafeClient;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.domain.SmsEquipment;
import com.dico.result.ResultData;
import feign.hystrix.FallbackFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-04-29 11:14
 */
@Component
// spring动态代理设置为cglib代理才能够使用熔断机制
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ConstructionSafeHystrix implements FallbackFactory<ConstructionSafeClient> {
    @Override
    public ConstructionSafeClient create(Throwable throwable) {
        return new ConstructionSafeClient() {
            @Override
            public List<SmsEquipment> findByClassId(String classId) {
                return null;
            }

            @Override
            public SmsEquipment smsEquipmentDataInfo(String smsEquipmentId) {
                return null;
            }


        };
    }
}
