package com.dico.modules.dto;

import com.dico.annotation.ValidatedNotEmpty;
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

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "保养列表实体")
public class SmsUserMaintenanceListDto {

    @Id
    @ApiModelProperty(name = "id", value = "保养ID")
    private String id;

    @ApiModelProperty(name = "equipmentCode", value = "设备编号")
    private String equipmentCode;

    @ApiModelProperty(name = "equipmentName", value = "设备名称")
    private String equipmentName;

    @ApiModelProperty(name = "status", value = "状态（0待保养1已保养2逾期）")
    private int status;

    @ApiModelProperty(name = "cycle", value = "周期（0周1月2季3年）")
    private int cycle;

    @ApiModelProperty(name = "installArea", value = "安装区域")
    private String installArea;

    @ApiModelProperty(name = "remark", value = "复查要求")
    private String remark;

    @ApiModelProperty(name = "maintenanceOrganization", value = "保养单位")
    private String maintenanceOrganization;

    @ApiModelProperty(name = "maintenanceUser", value = "保养人")
    private String maintenanceUser;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "endDate", value = "保养期限")
    private Date endDate;
}
