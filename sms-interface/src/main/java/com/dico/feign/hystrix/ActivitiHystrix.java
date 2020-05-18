package com.dico.feign.hystrix;

import com.dico.feign.domain.Cost;
import com.dico.feign.feignClient.ActivitiClient;
import com.dico.result.ResultData;
import feign.hystrix.FallbackFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
// spring动态代理设置为cglib代理才能够使用熔断机制
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ActivitiHystrix implements FallbackFactory<ActivitiClient> {

    @Override
    public ActivitiClient create(Throwable throwable) {
        return new ActivitiClient(){
            @Override
            public ResultData startActiviti(Cost cost) {
                return ResultData.getFailResult(throwable.getMessage());
            }
        };
    }
}
