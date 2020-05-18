package com.dico.modules.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.activiti.engine.task.Task;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("费用申请实体")
public class CostApply implements Serializable{
    @ApiModelProperty(name = "id", value = "主键", required = true)
    int id;
    @ApiModelProperty(name = "process_instance_id", value = "流程实例id", hidden = true)
    String process_instance_id;
    @ApiModelProperty(name = "user_id", value = "用户名", hidden = true)
    String user_id;
    @ApiModelProperty(name = "type", value = "申请来源(1维修2保养)")
    int type;
    @ApiModelProperty(name = "cost_money", value = "申请金额")
    String cost_money;
    @ApiModelProperty(name = "apply_time", value = "申请时间", hidden = true)
    Date apply_time;
    @ApiModelProperty(name = "target_id", value = "目标ID")
    String target_id;
    @ApiModelProperty(name = "task", value = "关联的任务", readOnly = true, hidden = true)
    Task task;

}
