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

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "保养项实体")
public class SmsMaintenanceTargetDto {

    @Id
    @ApiModelProperty(name = "targetId", value = "保养项ID")
    private String targetId;

    @ApiModelProperty(name = "targetName", value = "保养项名称")
    private String targetName;
}
