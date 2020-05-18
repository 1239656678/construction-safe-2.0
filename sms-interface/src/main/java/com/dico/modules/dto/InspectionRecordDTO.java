package com.dico.modules.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "检查记录实体")
public class InspectionRecordDTO {

    @Id
    @ApiModelProperty(name = "statusId", value = "statusId")
    private String statusId;

    @ApiModelProperty(name = "address", value = "地址")
    private String address;

    @ApiModelProperty(name = "inspectionUser", value = "检查人")
    private String inspectionUser;

    @ApiModelProperty(name = "equipmentName", value = "设备名称")
    private String equipmentName;

    @ApiModelProperty(name = "inspectionDate", value = "检查时间")
    private Date inspectionDate;

    @ApiModelProperty(name = "instructions", value = "检查说明")
    private String instructions;

    @Transient
    @ApiModelProperty(name = "resultList", value = "检查结果集")
    private List<TargetResultDTO> resultList;

    @JsonBackReference
    private String result;

    @Transient
    @ApiModelProperty(name = "files", value = "上传的文件")
    private Object files;
}
