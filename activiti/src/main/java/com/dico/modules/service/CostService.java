package com.dico.modules.service;

import com.dico.modules.po.CostApply;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;
import java.util.Map;


public interface CostService {
	public ProcessInstance startWorkflow(CostApply costApply, String userid, Map<String, Object> variables);
	public CostApply getCostApply(int id);
	public List<CostApply> getWillDoing(String userid);
	public List<CostApply> getpageupdateapplytask(String userid);
	public int getallupdateapplytask(String userid);
	public void completereportback(String taskid, String realstart_time, String realend_time);
	public void updatecomplete(String taskid, CostApply costApply, String reappply);
	public List<String> getHighLightedFlows(ProcessDefinitionEntity deployedProcessDefinition, List<HistoricActivityInstance> historicActivityInstances);
}
