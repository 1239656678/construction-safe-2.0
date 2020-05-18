package com.dico.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "检查项")
public class TargetResultDTO {
    @ApiModelProperty(name = "id", value = "检查项ID", required = true)
    private String id;
    @ApiModelProperty(name = "result", value = "检查项结果(true正常false异常)", required = true)
    private boolean result;
}
