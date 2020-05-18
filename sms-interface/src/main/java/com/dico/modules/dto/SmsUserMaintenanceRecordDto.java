package com.dico.modules.dto;

import com.dico.annotation.ValidatedNotEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "保养记录实体")
public class SmsUserMaintenanceRecordDto {

    @ValidatedNotEmpty
    @ApiModelProperty(name = "userMaintenancePlanId", value = "用户保养计划ID")
    private String userMaintenancePlanId;

    @ApiModelProperty(name = "smsMaintenanceItemList", value = "保养项")
    private List<SmsMaintenanceItemDto> smsMaintenanceItemList;

    @ApiModelProperty(name = "smsMaintenanceResult", value = "保养结果")
    private SmsMaintenanceResultDto smsMaintenanceResult;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "remark", value = "保养描述")
    private String remark;

    @Transient
    @ValidatedNotEmpty
    @ApiModelProperty(name = "files", value = "上传文件返回的ID")
    private String[] files;
}
