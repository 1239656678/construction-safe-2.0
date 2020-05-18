package com.dico.modules.domain;

import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import com.dico.annotation.ViewField;
import com.dico.enums.DeviceEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "设备类型实体")
@Table(name = "sms_equipment_class")
public class SmsEquipmentClass {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "ID", value = "主键")
    private String id;

    @ApiModelProperty(name = "classCode", value = "类型编号", required = true)
    private String classCode;

    @ApiModelProperty(name = "className", value = "类型名称", required = true)
    private String className;

    @ApiModelProperty(name = "parentClass", value = "上级类型", required = true)
    private String parentClass;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

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
    private List<SmsEquipmentClass> children;

    @Transient
    private List<SmsEquipment> smsEquipmentList;


    @Transient
    private String test;


    @Transient
    @JsonIgnoreProperties
    @ApiModelProperty(name = "checkItemIds", value = "检查项目", hidden = true)
    private String checkItemIds;
}
