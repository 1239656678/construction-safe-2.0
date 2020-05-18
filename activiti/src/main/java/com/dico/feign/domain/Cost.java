package com.dico.feign.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("费用申请实体")
public class Cost {
    @ApiModelProperty(name = "repairId", value = "整改单ID", required = true)
    private String repairId;
    @ApiModelProperty(name = "costMoney", value = "金额", required = true)
    private int costMoney;
    @ApiModelProperty(name = "type", value = "来源{1维修2保养}", required = true)
    private int type;
}
