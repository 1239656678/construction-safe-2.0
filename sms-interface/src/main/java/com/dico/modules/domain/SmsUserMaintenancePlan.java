package com.dico.modules.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Administrator
 * @date 2020-04-02 11:56
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户保养计划实体")
@Table(name = "sms_user_maintenance_plan", schema = "construction_safe", catalog = "")
public class SmsUserMaintenancePlan {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(value = "主键", name = "id")
    private String id;

    @ApiModelProperty(value = "保养计划ID", name = "maintenancePlanId", required = true)
    private String maintenancePlanId;

    @ApiModelProperty(value = "周期名称", name = "cycle", required = true)
    private String cycle;

    @ApiModelProperty(value = "状态(0待保养1已保养2逾期)", name = "status", required = true)
    private int status;

    @ApiModelProperty(value = "开始时间", name = "beginDate", required = true)
    private Date beginDate;

    @ApiModelProperty(value = "结束时间", name = "endDate", required = true)
    private Date endDate;

    @ApiModelProperty(value = "备注", name = "remark")
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
