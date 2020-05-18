package com.dico.modules.pagemodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("费用申请任务信息")
public class CostTask {
	@ApiModelProperty("主键")
	int id;
	@ApiModelProperty("流程实例id")
	String processInstanceId;
	@ApiModelProperty("用户ID")
	String user_id;
	@ApiModelProperty("类型")
	int type;
	@ApiModelProperty("金额")
	String costMoney;
	@ApiModelProperty("目标")
	String targetId;
	@ApiModelProperty("申请时间")
	Date apply_time;
	@ApiModelProperty("任务id")
	String taskid;
	@ApiModelProperty("任务名")
	String taskname;
	@ApiModelProperty("流程定义id")
	String processdefid;
	@ApiModelProperty("任务创建时间")
	Date taskCreatetime;
	
}
