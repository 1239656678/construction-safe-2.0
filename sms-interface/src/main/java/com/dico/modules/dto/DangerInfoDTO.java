package com.dico.modules.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "隐患信息实体")
public class DangerInfoDTO {

    @ApiModelProperty(name = "repairUserID", value = "整改人ID")
    private String repairUserID;

    @ApiModelProperty(name = "reviewUserID", value = "复查人ID")
    private String reviewUserID;

    @ApiModelProperty(name = "dangerLevelId", value = "隐患级别(0一般隐患1较大隐患2重大隐患)")
    private int dangerLevelId;

    @ApiModelProperty(name = "equipmentId", value = "设备ID")
    private String equipmentId;

    @ApiModelProperty(name = "remark", value = "隐患描述", required = true)
    private String remark;

    @ApiModelProperty(name = "address", value = "隐患地址", required = true)
    private String address;

    @ApiModelProperty(name = "repairLimit", value = "整改期限")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date repairLimit;

    @ApiModelProperty(name = "repairOpinion", value = "整改要求")
    private String repairOpinion;

    @ApiModelProperty(name = "fileIds", value = "上传文件返回的ID", required = true)
    private String[] fileIds;
}
