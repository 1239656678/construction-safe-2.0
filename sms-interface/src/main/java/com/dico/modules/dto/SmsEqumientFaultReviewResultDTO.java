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
@ApiModel(value = "维修复查实体")
public class SmsEqumientFaultReviewResultDTO {
    @Id
    @ApiModelProperty(name = "id", value = "维修详情id")
    private String faultInfoId;
    @ApiModelProperty(name = "planId", value = "维修记录id", required = true)
    private String faultRecordId;
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
