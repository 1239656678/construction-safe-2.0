package com.dico.modules.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-04-02 11:56
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "保养计划实体")
@Table(name = "sms_maintenance_plan")
public class SmsMaintenancePlan {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(value = "主键", name = "id")
    private String id;

    @ApiModelProperty(value = "设备ID", name = "equipmentId", required = true)
    private String equipmentId;

    @ApiModelProperty(value = "周期(0周1月2季3年)", name = "cycle", required = true)
    private int cycle;

    @ApiModelProperty(value = "保养人", name = "maintenanceUser", required = true)
    private String maintenanceUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间", name = "beginDate", required = true)
    private Date beginDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间", name = "endDate", required = true)
    private Date endDate;

    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

    @ApiModelProperty(value = "createUser", name = "创建人", hidden = true)
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "createDate", name = "创建时间", hidden = true)
    private Date createDate;

    @ApiModelProperty(value = "updateUser", name = "修改人", hidden = true)
    private String updateUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "updateDate", name = "修改时间", hidden = true)
    private Date updateDate;

    @ApiModelProperty(value = "delFlag", name = "删除标识", hidden = true)
    private boolean delFlag;
}
