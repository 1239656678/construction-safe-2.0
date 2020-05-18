package com.dico.modules.service.impl;

import com.dico.feign.domain.CurrentSysMenu;
import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.mapper.CostApplyMapper;
import com.dico.modules.po.CostApply;
import com.dico.modules.service.CostService;
import com.dico.result.ResultData;
import com.netflix.discovery.converters.Auto;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

//@Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.DEFAULT,timeout=5)
@Service
public class CostServiceImpl implements CostService {
    @Resource
    CostApplyMapper costApplyMapper;
    @Resource
    IdentityService identityservice;
    @Resource
    RuntimeService runtimeservice;
    @Resource
    TaskService taskservice;

    @Autowired
    DicoBaseClient dicoBaseClient;

    public ProcessInstance startWorkflow(CostApply costApply, String userid, Map<String, Object> variables) {
        costApply.setApply_time(new Date());
        costApply.setUser_id(userid);
        costApplyMapper.save(costApply);
        String businesskey = String.valueOf(costApply.getId());//使用表的主键作为businesskey,连接业务数据和流程数据
        identityservice.setAuthenticatedUserId(userid);
        ProcessInstance instance = runtimeservice.startProcessInstanceByKey("cost", businesskey, variables);
        System.out.println(businesskey);
        String instanceid = instance.getId();
        costApply.setProcess_instance_id(instanceid);
        costApplyMapper.updateByPrimaryKey(costApply);
        return instance;
    }

    public CostApply getCostApply(int id) {
        CostApply leave = costApplyMapper.getCostApply(id);
        return leave;
    }

    @Override
    public List<CostApply> getWillDoing(String userid) {
        List<CostApply> results = new ArrayList<CostApply>();
        List<String> roles = new ArrayList<String>();
        roles.add("费用审核人");
        roles.add("调整申请");
        List<Task> tasks = taskservice.createTaskQuery().taskNameIn(roles).list();
        for (Task task : tasks) {
            String instanceid = task.getProcessInstanceId();
            ProcessInstance ins = runtimeservice.createProcessInstanceQuery().processInstanceId(instanceid).singleResult();
            String businesskey = ins.getBusinessKey();
            CostApply a = costApplyMapper.getCostApply(Integer.parseInt(businesskey));
            a.setTask(task);
            results.add(a);
        }
        return results;
    }

    public List<CostApply> getpageupdateapplytask(String userid) {
        List<CostApply> results = new ArrayList<CostApply>();
        List<Task> tasks = taskservice.createTaskQuery().taskName("调整申请").list();
        for (Task task : tasks) {
            String instanceid = task.getProcessInstanceId();
            ProcessInstance ins = runtimeservice.createProcessInstanceQuery().processInstanceId(instanceid).singleResult();
            String businesskey = ins.getBusinessKey();
            CostApply a = costApplyMapper.getCostApply(Integer.parseInt(businesskey));
            a.setTask(task);
            results.add(a);
        }
        return results;
    }

    public int getallupdateapplytask(String userid) {
        List<Task> tasks = taskservice.createTaskQuery().taskCandidateOrAssigned(userid).taskName("调整申请").list();
        return tasks.size();
    }

    public void completereportback(String taskid, String realstart_time, String realend_time) {
        Task task = taskservice.createTaskQuery().taskId(taskid).singleResult();
        String instanceid = task.getProcessInstanceId();
        ProcessInstance ins = runtimeservice.createProcessInstanceQuery().processInstanceId(instanceid).singleResult();
        String businesskey = ins.getBusinessKey();
        CostApply a = costApplyMapper.getCostApply(Integer.parseInt(businesskey));
        costApplyMapper.updateByPrimaryKey(a);
        taskservice.complete(taskid);
    }

    public void updatecomplete(String taskid, CostApply costApply, String updateApply) {
        Task task = taskservice.createTaskQuery().taskId(taskid).singleResult();
        String instanceid = task.getProcessInstanceId();
        ProcessInstance ins = runtimeservice.createProcessInstanceQuery().processInstanceId(instanceid).singleResult();
        String businesskey = ins.getBusinessKey();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("updateApply", updateApply);
        if (updateApply.toLowerCase().trim().equals("true")) {
            costApplyMapper.updateByPrimaryKey(costApply);
            taskservice.complete(taskid, variables);
        } else {
            taskservice.complete(taskid, variables);
        }
    }

    public List<String> getHighLightedFlows(
            ProcessDefinitionEntity processDefinitionEntity,
            List<HistoricActivityInstance> historicActivityInstances) {

        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
        for (int i = 0; i < historicActivityInstances.size(); i++) {// 对历史流程节点进行遍历
            ActivityImpl activityImpl = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i)
                            .getActivityId());// 得 到节点定义的详细信息
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
            if ((i + 1) >= historicActivityInstances.size()) {
                break;
            }
            ActivityImpl sameActivityImpl1 = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i + 1)
                            .getActivityId());// 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances
                        .get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances
                        .get(j + 1);// 后续第二个节点
                if (activityImpl1.getStartTime().equals(
                        activityImpl2.getStartTime())) {// 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity
                            .findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {// 有不相同跳出循环
                    break;
                }
            }
            List<PvmTransition> pvmTransitions = activityImpl
                    .getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {// 对所有的线进行遍历
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition
                        .getDestination();// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }
}
