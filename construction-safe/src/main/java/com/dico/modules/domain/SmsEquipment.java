package com.dico.modules.domain;

import com.dico.modules.dto.SmsEquipmentFaultInfoDto;
import com.dico.modules.dto.SmsUserMaintenanceListDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@ApiModel(value = "设备信息实体")
@Table(name = "sms_equipment")
public class SmsEquipment {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    @ApiModelProperty(name = "code", value = "设备编号", required = true)
    private String code;
    @ApiModelProperty(name = "name", value = "设备名称", required = true)
    private String name;
    @ApiModelProperty(name = "typeId", value = "设备分类ID", required = true)
    private String typeId;
    @Transient
    @ApiModelProperty(name = "typeName", value = "设备分类名称")
    private String typeName;
    @ApiModelProperty(name = "model", value = "设备型号", required = true)
    private String model;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "produceDate", value = "生产日期", required = true)
    private Date produceDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "scrappedDate", value = "报废日期", required = true)
    private Date scrappedDate;
    @ApiModelProperty(name = "status", value = "设备状态（1正常2故障）", required = true)
    private Byte status;
    @ApiModelProperty(name = "responsibleOrganizationId", value = "责任部门ID", required = true)
    private String responsibleOrganizationId;
    @Transient
    @ApiModelProperty(name = "responsibleOrganizationName", value = "责任部门名称")
    private String responsibleOrganizationName;
    @ApiModelProperty(name = "responsibleUserId", value = "责任人ID", required = true)
    private String responsibleUserId;
    @Transient
    @ApiModelProperty(name = "responsibleUserName", value = "责任人名称")
    private String responsibleUserName;
    @ApiModelProperty(name = "producer", value = "生产厂家")
    private String producer;
    @ApiModelProperty(name = "installRegionsId", value = "安装区域ID")
    private String installRegionsId;
    @Transient
    @ApiModelProperty(name = "installRegionsName", value = "安装区域名称")
    private String installRegionsName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "installDate", value = "安装时间")
    private Date installDate;
    @ApiModelProperty(name = "qrCode", value = "二维码地址")
    private String qrCode;
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;
    @ApiModelProperty(name = "createUser", value = "创建人ID", hidden = true)
    private String createUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "createDate", value = "创建时间", hidden = true)
    private Date createDate;
    @ApiModelProperty(name = "updateUser", value = "修改人ID", hidden = true)
    private String updateUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "updateDate", value = "修改时间", hidden = true)
    private Date updateDate;
    @ApiModelProperty(name = "delFlag", value = "数据状态", hidden = true)
    private boolean delFlag;

    @Transient
    private boolean generatorQr;

    @Transient
    @ApiModelProperty(name = "smsUserMaintenanceListDtoList", value = "设备保养记录", hidden = true)
    private List<SmsUserMaintenanceListDto> smsUserMaintenanceListDtoList;

    @Transient
    @ApiModelProperty(name = "smsEquipmentFaultInfoDtoList", value = "设备维修记录", hidden = true)
    private List<SmsEquipmentFaultInfoDto> smsEquipmentFaultInfoDtoList;
}
