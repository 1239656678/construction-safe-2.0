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
@ApiModel(value = "保养结果实体")
@Table(name = "sms_maintenance_after", schema = "construction_safe", catalog = "")
public class SmsMaintenanceAfter {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(value = "主键", name = "id")
    private String id;

    @ApiModelProperty(value = "用户保养计划ID", name = "userMaintenanceId", required = true)
    private String userMaintenanceId;

    @ApiModelProperty(value = "金额", name = "cost", required = true)
    private String cost;

    @ApiModelProperty(value = "描述", name = "remark", required = true)
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
