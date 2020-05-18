package com.dico.modules.dto;

import com.dico.annotation.ValidatedNotEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "保养项结果实体")
public class SmsMaintenanceItemDto {

    @ValidatedNotEmpty
    @ApiModelProperty(name = "targetId", value = "保养项ID")
    private String targetId;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "targetName", value = "保养项名称")
    private String targetName;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "result", value = "保养结果false正常true不正常")
    private Boolean result;
}
