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
@ApiModel(value = "设备巡检项绑定实体")
@Table(name = "sms_equipment_class_target")
public class SmsEquipmentClassTarget {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "ID", value = "主键")
    private String id;

    @ApiModelProperty(name = "equipmentClassId", value = "设备类型ID")
    private String equipmentClassId;

    @ApiModelProperty(name = "targetId", value = "检查项ID")
    private String targetId;

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
}
