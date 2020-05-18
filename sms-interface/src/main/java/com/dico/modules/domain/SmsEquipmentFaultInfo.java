package com.dico.modules.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "设备故障实体")
@Table(name = "sms_equipment_fault_info")
public class SmsEquipmentFaultInfo {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "ID", value = "主键", hidden = true)
    private String id;

    @ApiModelProperty(name = "equipmentId", value = "设备ID", required = true)
    private String equipmentId;

    @ApiModelProperty(name = "reportUserId", value = "上报人", hidden = true)
    private String reportUserId;

    @ApiModelProperty(name = "reportDate", value = "上报时间", hidden = true)
    private Date reportDate;

    @ApiModelProperty(name = "status", value = "故障状态（false待维修true已维修）")
    private boolean status;

    @ApiModelProperty(name = "status", value = "是否指派（false未指派true已指派）", hidden = true)
    private boolean isAssign;

    @ApiModelProperty(name = "remark", value = "故障描述", required = true)
    private String remark;

    @ApiModelProperty(name = "reviewResult", value = "复查描述")
    private String reviewResult;

    @ApiModelProperty(name = "createUser", value = "创建人", hidden = true)
    private String createUser;

    @ApiModelProperty(name = "createDate", value = "创建时间", hidden = true)
    private Date createDate;

    @ApiModelProperty(name = "updateUser", value = "修改人", hidden = true)
    private String updateUser;

    @ApiModelProperty(name = "updateDate", value = "修改时间", hidden = true)
    private Date updateDate;

    @ApiModelProperty(name = "delFlag", value = "删除标识", hidden = true)
    private boolean delFlag;

    @Transient
    @ApiModelProperty(name = "fileIds", value = "上传附件的ID", required = true)
    private String[] fileIds;
}
