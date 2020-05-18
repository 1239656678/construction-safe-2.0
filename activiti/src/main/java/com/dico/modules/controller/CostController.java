package com.dico.modules.controller;

import com.dico.feign.feignClient.DicoBaseClient;
import com.dico.modules.pagemodel.CostTask;
import com.dico.modules.pagemodel.HistoryProcess;
import com.dico.modules.pagemodel.MSG;
import com.dico.modules.pagemodel.RunningProcess;
import com.dico.modules.po.CostApply;
import com.dico.modules.service.CostService;
import com.dico.result.ResultData;
import com.dico.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "费用申请流程接口", produces = "费用申请流程接口Api")
public class CostController {

    @Autowired
    CostService costService;

    @Autowired
    DicoBaseClient dicoBaseClient;

    @Resource
    TaskService taskservice;

    @Resource
    RuntimeService runservice;

    @Resource
    HistoryService historyService;

    /**
     * 开始费用申请流程
     *
     * @param request
     * @param costApply
     * @return
     */
    @ResponseBody
    @PostMapping("/cost/startCost")
    @ApiOperation(value = "创建费用申请流程", notes = "创建费用申请流程API")
    public ResultData start_test(HttpServletRequest request, @RequestBody CostApply costApply) {
        String userid = TokenUtils.getUserIdByRequest(request);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applyuserid", userid);
        ProcessInstance ins = costService.startWorkflow(costApply, userid, variables);
        System.out.println("流程id" + ins.getId() + "已启动");
        return ResultData.getSuccessResult();
    }

    /**
     * 我的待办
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/cost/getWillDoing")
    @ApiOperation(value = "我的待办", notes = "我的待办")
    public ResultData getWillDoing(HttpServletRequest request) {
        List<CostApply> costApplyList = costService.getWillDoing(TokenUtils.getUserIdByRequest(request));
        List<CostTask> tasks = new ArrayList<CostTask>();
        for (CostApply apply : costApplyList) {
            CostTask task = new CostTask();
            task.setApply_time(apply.getApply_time());
            task.setUser_id(apply.getUser_id());
            task.setId(apply.getId());
            task.setType(apply.getType());
            task.setProcessInstanceId(apply.getProcess_instance_id());
            task.setProcessdefid(apply.getTask().getProcessDefinitionId());
            task.setTaskCreatetime(apply.getTask().getCreateTime());
            task.setTaskid(apply.getTask().getId());
            task.setTaskname(apply.getTask().getName());
            tasks.add(task);
        }
        return ResultData.getSuccessResult(tasks);
    }

    /**
     * 费用审批详情
     *
     * @param taskid
     * @return
     */
    @ResponseBody
    @GetMapping("/cost/dealtask")
    @ApiOperation(value = "费用审批详情", notes = "费用审批详情")
    public ResultData taskdeal(@RequestParam("taskid") String taskid) {
        Task task = taskservice.createTaskQuery().taskId(taskid).singleResult();
        ProcessInstance process = runservice.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId())
                .singleResult();
        CostApply costApply = costService.getCostApply(new Integer(process.getBusinessKey()));
        return ResultData.getSuccessResult(costApply);
    }

    /**
     * 费用审批
     *
     * @param request
     * @param taskid
     * @return
     */
    @ResponseBody
    @PostMapping("/cost/task/deptcomplete")
    @ApiOperation(value = "费用审批", notes = "费用审批")
    public ResultData deptcomplete(HttpServletRequest request, @RequestParam("taskid") String taskid, @RequestParam("costApprovalRes") boolean costApprovalRes) {
        String userid = TokenUtils.getUserIdByRequest(request);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("costApprovalRes", costApprovalRes);
        taskservice.claim(taskid, userid);
        taskservice.complete(taskid, variables);
        return ResultData.getSuccessResult();
    }

    /**
     * 调整申请待办列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/cost/updateapply")
    @ApiOperation(value = "调整申请待办列表", notes = "调整申请待办列表")
    public ResultData updateapply(HttpServletRequest request) {
        String userid = TokenUtils.getUserIdByRequest(request);
        List<CostApply> result = costService.getpageupdateapplytask(userid);
        List<CostTask> tasks = new ArrayList<CostTask>();
        for (CostApply costApply : result) {
            CostTask task = new CostTask();
            task.setApply_time(costApply.getApply_time());
            task.setUser_id(costApply.getUser_id());
            task.setId(costApply.getId());
            task.setType(costApply.getType());
            task.setCostMoney(costApply.getCost_money());
            task.setTargetId(costApply.getTarget_id());
            task.setProcessInstanceId(costApply.getProcess_instance_id());
            task.setProcessdefid(costApply.getTask().getProcessDefinitionId());
            task.setTaskid(costApply.getTask().getId());
            task.setTaskname(costApply.getTask().getName());
            tasks.add(task);
        }
        return ResultData.getSuccessResult(tasks);
    }

    /**
     * 调整申请
     *
     * @param taskid
     * @param updateApply
     * @param id
     * @param costMoney
     * @return
     */
    @ResponseBody
    @PostMapping("/cost/task/updateapplycomplete/{taskid}")
    @ApiOperation(value = "调整申请", notes = "调整申请")
    public MSG updateapplycomplete(@PathVariable("taskid") String taskid, @RequestParam("updateApply") boolean updateApply, String id, String costMoney) {
        CostApply _costApply = costService.getCostApply(Integer.valueOf(id));
        _costApply.setCost_money(costMoney);
        costService.updatecomplete(taskid, _costApply, String.valueOf(updateApply));
        return new MSG("ok");
    }

    /**
     * 我发起的已完成的流程
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/cost/getStartByMe")
    @ApiOperation(value = "我发起的", notes = "我发起的")
    public ResultData getStartByMe(HttpServletRequest request) {
        String userid = TokenUtils.getUserIdByRequest(request);
        HistoricProcessInstanceQuery process = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey("cost").startedBy(userid);
        List<HistoricProcessInstance> info = process.list();
        List<HistoryProcess> list = new ArrayList<HistoryProcess>();
        for (HistoricProcessInstance history : info) {
            HistoryProcess his = new HistoryProcess();
            String bussinesskey = history.getBusinessKey();
            CostApply apply = costService.getCostApply(Integer.parseInt(bussinesskey));
            his.setCostApply(apply);
            his.setBusinessKey(bussinesskey);
            his.setProcessDefinitionId(history.getProcessDefinitionId());
            list.add(his);
        }
        return ResultData.getSuccessResult(list);
    }

    /**
     * 我发起的正在运行的流程
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/cost/setupprocess")
    @ApiOperation(value = "我发起的正在运行的", notes = "我发起的正在运行的")
    public ResultData setupprocess(HttpServletRequest request) {
        String userid = TokenUtils.getUserIdByRequest(request);
        ProcessInstanceQuery query = runservice.createProcessInstanceQuery();
        List<ProcessInstance> a = query.processDefinitionKey("cost").involvedUser(userid).list();
        List<RunningProcess> list = new ArrayList<RunningProcess>();
        for (ProcessInstance p : a) {
            RunningProcess process = new RunningProcess();
            process.setActivityid(p.getActivityId());
            process.setBusinesskey(p.getBusinessKey());
            process.setExecutionid(p.getId());
            process.setProcessInstanceid(p.getProcessInstanceId());
            CostApply costApply = costService.getCostApply(Integer.parseInt(p.getBusinessKey()));
            if (costApply.getUser_id().equals(userid))
                list.add(process);
            else
                continue;
        }
        return ResultData.getSuccessResult(list);
    }

    /**
     * 参与的正在运行的费用申请流程
     *
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping("/cost/involvedprocess")
    @ApiOperation(value = "参与的正在运行的", notes = "参与的正在运行的")
    public ResultData allexeution(HttpServletRequest request) {
        String userid = TokenUtils.getUserIdByRequest(request);
        ProcessInstanceQuery query = runservice.createProcessInstanceQuery();
        int total = (int) query.count();
        List<ProcessInstance> a = query.processDefinitionKey("cost").involvedUser(userid).list();
        List<RunningProcess> list = new ArrayList<RunningProcess>();
        for (ProcessInstance p : a) {
            RunningProcess process = new RunningProcess();
            process.setActivityid(p.getActivityId());
            process.setBusinesskey(p.getBusinessKey());
            process.setExecutionid(p.getId());
            process.setProcessInstanceid(p.getProcessInstanceId());
            list.add(process);
        }
        return ResultData.getSuccessResult(list);
    }
}
