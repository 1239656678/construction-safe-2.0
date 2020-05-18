package com.dico.modules.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "巡检检查标准实体")
@Table(name = "sms_inspection_target")
public class SmsInspectionTarget {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(value = "主键", name = "id")
    private String id;

    @ApiModelProperty(value = "检查项编码", name = "targetCode", required = true)
    private String targetCode;

    @ApiModelProperty(value = "检查项名称", name = "targetName", required = true)
    private String targetName;

    @ApiModelProperty(value = "检查项描述", name = "remark", required = true)
    private String remark;

    @ApiModelProperty(value = "createUser", name = "创建人", hidden = true)
    private String createUser;

    @ApiModelProperty(value = "createDate", name = "创建时间", hidden = true)
    private Date createDate;

    @ApiModelProperty(value = "updateUser", name = "修改人", hidden = true)
    private String updateUser;

    @ApiModelProperty(value = "updateDate", name = "修改时间", hidden = true)
    private Date updateDate;

    @ApiModelProperty(value = "delFlag", name = "删除标识", hidden = true)
    private boolean delFlag;

}
