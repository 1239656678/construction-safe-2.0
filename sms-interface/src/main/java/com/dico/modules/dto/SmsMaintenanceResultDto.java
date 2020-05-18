package com.dico.modules.dto;

import com.dico.annotation.ValidatedNotEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "保养结果实体")
public class SmsMaintenanceResultDto {

    @ValidatedNotEmpty
    @ApiModelProperty(name = "cost", value = "保养费用")
    private String cost;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "result", value = "保养结果")
    private String result;

    @Transient
    @ValidatedNotEmpty
    @ApiModelProperty(name = "files", value = "上传文件返回的ID")
    private String[] files;
}
