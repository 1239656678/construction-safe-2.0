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
 * @date 2020-05-07 10:15
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "保养复查实体")
@Table(name = "sms_user_maintenance_plan_review", schema = "construction_safe", catalog = "")
public class SmsUserMaintenancePlanReview {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(value = "主键", name = "id")
    private String id;
    @ApiModelProperty(value = "maintainId", name = "保养计划ID", required = true)
    private String maintainId;
    @ApiModelProperty(value = "maintainOrganizationId", name = "组织机构ID", required = true)
    private String maintainOrganizationId;
    @ApiModelProperty(value = "maintainUserId", name = "指定复查人ID", required = true)
    private String maintainUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "maintainLimit", name = "复查期限", required = true)
    private Date maintainLimit;
    @ApiModelProperty(value = "maintainStatus", name = "复查状态(0：待复查,1:未通过，2：已复查，）", required = true)
    private Integer maintainStatus;
    @ApiModelProperty(value = "maintainResult", name = "复查结果", required = true)
    private String maintainResult;
    @ApiModelProperty(value = "createUser", name = "创建人", hidden = true)
    private String createUser;
    @ApiModelProperty(value = "createDate", name = "创建时间", hidden = true)
    private Date createDate;
    @ApiModelProperty(value = "updateUser", name = "修改人", hidden = true)
    private String updateUser;
    @ApiModelProperty(value = "updateDate", name = "更新时间", hidden = true)
    private Date updateDate;
    @ApiModelProperty(value = "delFlag", name = "删除标识", hidden = true)
    private Boolean delFlag;
    @Transient
    private Object files;
    @Transient
    private boolean changeDangerStatus = false;


}
