package com.dico.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "设备分类")
public class EquipmentClassDTO {
    @ApiModelProperty(name = "id", value = "设备分类ID", required = true)
    private String id;
    @ApiModelProperty(name = "name", value = "设备分类名称", required = true)
    private String name;
}
