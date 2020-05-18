package com.dico.modules.dto;

import com.dico.annotation.ValidatedNotEmpty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "保养记录详情实体")
public class SmsMaintenanceRecordInfoDto {

    @Id
    @ValidatedNotEmpty
    @ApiModelProperty(name = "id", value = "id")
    private String id;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "equipmentName", value = "设备名称")
    private String equipmentName;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "equipmentCode", value = "设备编号")
    private String equipmentCode;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "status", value = "状态（0待保养1已保养2逾期）")
    private int status;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "installArea", value = "安装区域")
    private String installArea;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "maintenanceOrganization", value = "保养部门")
    private String maintenanceOrganization;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "maintenanceUser", value = "保养人")
    private String maintenanceUser;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "cycle", value = "周期（0周1月2季3年）")
    private int cycle;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "cost", value = "费用")
    private String cost;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "remark", value = "保养描述")
    private String remark;

    @ValidatedNotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(name = "finishDate", value = "完成时间")
    private Date finishDate;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "result", value = "保养结果")
    private String result;

    @JsonIgnore
    @ApiModelProperty(name = "smbId", value = "保养前ID")
    private String smbId;

    @JsonIgnore
    @ApiModelProperty(name = "smaId", value = "保养后ID")
    private String smaId;

    @Transient
    @ApiModelProperty(name = "targetId", value = "保养检查附件")
    private Object beforeFiles;

    @Transient
    @ApiModelProperty(name = "afterFiles", value = "保养结果附件")
    private Object afterFiles;

}
