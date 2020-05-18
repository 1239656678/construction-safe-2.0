package com.dico.modules.controller;

import com.dico.modules.pagemodel.*;
import com.dico.modules.pagemodel.Process;
import com.dico.modules.service.CostService;
import com.dico.result.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "流程基础接口", produces = "流程基础接口Api")
public class ActivitiController {
    @Resource
    RepositoryService rep;
    @Resource
    HistoryService histiryservice;

    @Resource
    RuntimeService runservice;

    @Resource
    CostService costService;

    /**
     * 工作流发布
     *
     * @param uploadfile
     * @param request
     * @return
     */
    @PostMapping("/baseActiviti/uploadworkflow")
    @ApiOperation(value = "上传工作流BPMN", notes = "上传工作流BPMN")
    public ResultData fileupload(@RequestParam MultipartFile uploadfile, HttpServletRequest request) {
        try {
            MultipartFile file = uploadfile;
            String filename = file.getOriginalFilename();
            InputStream is = file.getInputStream();
            rep.createDeployment().addInputStream(filename, is).deploy();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getFailResult(e.getMessage());
        }
        return ResultData.getSuccessResult();
    }

    /**
     * 获取已经发布的流程
     *
     * @param current
     * @param rowCount
     * @return
     */
    @ResponseBody
    @PostMapping("/baseActiviti/getprocesslists")
    @ApiOperation(value = "获取已经发布的流程", notes = "获取已经发布的流程")
    public DataGrid<Process> getlist(@RequestParam("current") int current, @RequestParam("rowCount") int rowCount) {
        int firstrow = (current - 1) * rowCount;
        List<ProcessDefinition> list = rep.createProcessDefinitionQuery().listPage(firstrow, rowCount);
        int total = rep.createProcessDefinitionQuery().list().size();
        List<Process> mylist = new ArrayList<Process>();
        for (int i = 0; i < list.size(); i++) {
            Process p = new Process();
            p.setDeploymentId(list.get(i).getDeploymentId());
            p.setId(list.get(i).getId());
            p.setKey(list.get(i).getKey());
            p.setName(list.get(i).getName());
            p.setResourceName(list.get(i).getResourceName());
            p.setDiagramresourcename(list.get(i).getDiagramResourceName());
            mylist.add(p);
        }
        DataGrid<Process> grid = new DataGrid<Process>();
        grid.setCurrent(current);
        grid.setRowCount(rowCount);
        grid.setRows(mylist);
        grid.setTotal(total);
        return grid;
    }

    /**
     * 获取流程图或者流程定义的xml文件内容
     *
     * @param pdid
     * @param resource
     * @param response
     * @throws Exception
     */
    @GetMapping("/baseActiviti/showresource")
    @ApiOperation(value = "获取流程图或者流程定义的xml文件内容", notes = "获取流程图或者流程定义的xml文件内容")
    public void export(@RequestParam("pdid") String pdid, @RequestParam("resource") String resource,
                       HttpServletResponse response) throws Exception {
        ProcessDefinition def = rep.createProcessDefinitionQuery().processDefinitionId(pdid).singleResult();
        InputStream is = rep.getResourceAsStream(def.getDeploymentId(), resource);
        ServletOutputStream output = response.getOutputStream();
        IOUtils.copy(is, output);
    }

    /**
     * 删除已经部署的流程
     *
     * @param deployid
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/baseActiviti/deletedeploy")
    @ApiOperation(value = "删除已经部署的流程", notes = "删除已经部署的流程")
    public ResultData deletedeploy(@RequestParam("deployid") String deployid) throws Exception {
        rep.deleteDeployment(deployid, true);
        return ResultData.getSuccessResult();
    }

    /**
     * 流程实例详情
     *
     * @param instanceid
     * @return
     */
    @ResponseBody
    @PostMapping("/baseActiviti/processinfo")
    @ApiOperation(value = "流程实例详情", notes = "流程实例详情")
    public List<HistoricActivityInstance> processinfo(@RequestParam("instanceid") String instanceid) {
        List<HistoricActivityInstance> his = histiryservice.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceid).orderByHistoricActivityInstanceStartTime().asc().list();
        return his;
    }

    /**
     * 流程历史步骤
     *
     * @param ywh
     * @return
     */
    @ResponseBody
    @PostMapping("/baseActiviti/processhis")
    @ApiOperation(value = "流程历史步骤", notes = "流程历史步骤")
    public List<HistoricActivityInstance> processhis(@RequestParam("ywh") String ywh) {
        String instanceid = histiryservice.createHistoricProcessInstanceQuery().processDefinitionKey("purchase")
                .processInstanceBusinessKey(ywh).singleResult().getId();
        List<HistoricActivityInstance> his = histiryservice.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceid).orderByHistoricActivityInstanceStartTime().asc().list();
        return his;
    }

	/**
	 * 流程进度展示图
	 * @param executionid
	 * @param response
	 * @throws Exception
	 */
    @GetMapping("traceprocess/{executionid}")
	@ApiOperation(value = "流程进度展示图", notes = "流程进度展示图")
    public void traceprocess(@PathVariable("executionid") String executionid, HttpServletResponse response)
            throws Exception {
        ProcessInstance process = runservice.createProcessInstanceQuery().processInstanceId(executionid).singleResult();
        BpmnModel bpmnmodel = rep.getBpmnModel(process.getProcessDefinitionId());
        List<String> activeActivityIds = runservice.getActiveActivityIds(executionid);
        DefaultProcessDiagramGenerator gen = new DefaultProcessDiagramGenerator();
        // 获得历史活动记录实体（通过启动时间正序排序，不然有的线可以绘制不出来）
        List<HistoricActivityInstance> historicActivityInstances = histiryservice.createHistoricActivityInstanceQuery()
                .executionId(executionid).orderByHistoricActivityInstanceStartTime().asc().list();
        // 计算活动线
        List<String> highLightedFlows = costService
                .getHighLightedFlows(
                        (ProcessDefinitionEntity) ((RepositoryServiceImpl) rep)
                                .getDeployedProcessDefinition(process.getProcessDefinitionId()),
                        historicActivityInstances);

        InputStream in = gen.generateDiagram(bpmnmodel, "png", activeActivityIds, highLightedFlows, "宋体", "宋体", null,
                null, 1.0);
        // InputStream in=gen.generateDiagram(bpmnmodel, "png",
        // activeActivityIds);
        ServletOutputStream output = response.getOutputStream();
        IOUtils.copy(in, output);
    }

}

