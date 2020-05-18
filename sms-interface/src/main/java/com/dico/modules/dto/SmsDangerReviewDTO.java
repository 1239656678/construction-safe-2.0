package com.dico.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@ApiModel(value = "隐患复查实体")
public class SmsDangerReviewDTO {
    @Id
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    @ApiModelProperty(name = "reviewResult", value = "复查结果", required = true)
    private String reviewResult;
    @Transient
    @ApiModelProperty(name = "fileIds", value = "上传附件的ID", example = "ID,ID,ID,ID")
    private String[] fileIds;
}
