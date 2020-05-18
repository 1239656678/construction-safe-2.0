package com.dico.modules.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@ApiModel(value = "隐患信息实体")
@Table(name = "sms_danger_info", schema = "construction_safe", catalog = "")
public class SmsDangerInfo {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    @ApiModelProperty(name = "dangerFrom", value = "隐患来源(0随手拍1检查计划)", required = true)
    private int dangerFrom;
    @ApiModelProperty(name = "dangerTypeId", value = "隐患类型", required = true)
    private String dangerTypeId;
    @ApiModelProperty(name = "dangerAddress", value = "隐患地址", required = true)
    private String dangerAddress;
    @ApiModelProperty(name = "dangerLevelId", value = "隐患级别", required = true)
    private int dangerLevelId;
    @ApiModelProperty(name = "dangerStatus", value = "隐患状态(0未开始1待整改2待复查3已完成)", required = true)
    private int dangerStatus;
    @ApiModelProperty(name = "equipmentId", value = "设备ID", required = true)
    private String equipmentId;
    @ApiModelProperty(name = "remark", value = "隐患描述")
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
}
