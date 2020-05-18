package com.dico.common.filter;

import com.dico.common.config.ApplicationContextHeader;
import com.dico.modules.po.CostApply;
import com.dico.modules.service.CostService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ActivitiEndListener implements ExecutionListener {

    @Resource
    CostService costService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String costApplyId = delegateExecution.getProcessBusinessKey();
        if(costService == costService){
            costService = (CostService) ApplicationContextHeader.getBean(CostService.class);
        }
        CostApply costApply = costService.getCostApply(Integer.valueOf(costApplyId));
        System.out.println("流程结束"+delegateExecution.getEventName());
    }
}

