package com.dico.modules.pagemodel;

import com.dico.modules.po.CostApply;
import lombok.Data;

@Data
public class HistoryProcess {
	String processDefinitionId;
	String businessKey;
	CostApply costApply;
}
