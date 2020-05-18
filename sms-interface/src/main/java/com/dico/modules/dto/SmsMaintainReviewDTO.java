package com.dico.modules.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
@ApiModel(value = "隐患复查实体")
public class SmsMaintainReviewDTO {
    @Id
    @ApiModelProperty(name = "id", value = "复查id")
    private String mainid;

    @ApiModelProperty(name = "planId", value = "保养记录id", required = true)
    private String planId;
    @ApiModelProperty(name = "isStatus", value = "是否通过", required = true)
    private Boolean isStatus;
    @ApiModelProperty(name = "reviewResult", value = "复查结果", required = true)
    private String reviewResult;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "maintainLimit", name = "复查期限", required = true)
    private Date maintainLimit;
    @Transient
    @ApiModelProperty(name = "fileIds", value = "上传附件的ID", example = "ID,ID,ID,ID")
    private String[] fileIds;
}
