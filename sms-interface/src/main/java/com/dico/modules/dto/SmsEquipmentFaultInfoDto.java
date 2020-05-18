package com.dico.modules.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel(value = "故障实体")
public class SmsEquipmentFaultInfoDto {

    @Id
    @ApiModelProperty(name = "id", value = "故障单ID")
    private String id;

    @ApiModelProperty(name = "equipmentCode", value = "设备编号")
    private String equipmentCode;

    @ApiModelProperty(name = "equipmentName", value = "设备名称")
    private String equipmentName;

    @ApiModelProperty(name = "installArea", value = "安装区域")
    private String installArea;

    @ApiModelProperty(name = "reportUser", value = "上报人")
    private String reportUser;

    @ApiModelProperty(name = "reviewResult", value = "复查描述")
    private String reviewResult;

    @ApiModelProperty(name = "reportDate", value = "上报时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reportDate;

    @ApiModelProperty(name = "remark", value = "故障描述")
    private String remark;

    @ApiModelProperty(name = "status", value = "故障状态false待处理true已完成")
    private boolean status;

    @Transient
    private Object files;


}
