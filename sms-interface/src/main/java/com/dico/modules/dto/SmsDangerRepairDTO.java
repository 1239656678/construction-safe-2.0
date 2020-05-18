package com.dico.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@ApiModel(value = "隐患整改实体")
public class SmsDangerRepairDTO {
    @Id
    @ApiModelProperty(name = "id", value = "主键", required = true)
    private String id;
    @ApiModelProperty(name = "repairResult", value = "整改结果", required = true)
    private String repairResult;
    @ApiModelProperty(name = "hasCost", value = "是否需要成本", required = true)
    private boolean hasCost;
    @Transient
    @ApiModelProperty(name = "fileIds", value = "上传附件的ID", example = "ID,ID,ID,ID")
    private String[] fileIds;
}
