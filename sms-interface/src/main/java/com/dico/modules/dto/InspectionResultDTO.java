package com.dico.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "检查记录实体")
public class InspectionResultDTO {

    @ApiModelProperty(name = "statusId", value = "statusId")
    private String statusId;

    @ApiModelProperty(name = "instructions", value = "检查说明")
    private String instructions;

    @ApiModelProperty(name = "resultList", value = "检查结果集")
    private List<TargetResultDTO> resultList;

    @ApiModelProperty(name = "fileIds", value = "上传文件返回的ID")
    private String[] fileIds;
}
