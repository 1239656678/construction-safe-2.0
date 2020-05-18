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
@ApiModel(value = "维修记录实体")
public class SmsEquipmentFaultRecordDto {

    @Id
    @ValidatedNotEmpty
    @ApiModelProperty(name = "faultInfoId", value = "故障单ID")
    private String faultInfoId;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "result", value = "维修结果")
    private String result;

    @Transient
    @ValidatedNotEmpty
    @ApiModelProperty(name = "files", value = "上传文件返回的ID")
    private String[] files;
}
