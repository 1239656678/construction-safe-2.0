package com.dico.modules.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@ApiModel(value = "隐患复查实体")
@Table(name = "sms_danger_review", schema = "construction_safe", catalog = "")
public class SmsDangerReview {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    @ApiModelProperty(name = "dangerId", value = "隐患ID", required = true)
    private String dangerId;
    @ApiModelProperty(name = "reviewOrganizationId", value = "复查组织机构", required = true)
    private String reviewOrganizationId;
    @ApiModelProperty(name = "reviewUserId", value = "复查人ID", required = true)
    private String reviewUserId;
    @ApiModelProperty(name = "reviewLimit", value = "复查期限", required = true)
    private Date reviewLimit;
    @ApiModelProperty(name = "reviewStatus", value = "复查状态", required = true)
    private boolean reviewStatus;
    @ApiModelProperty(name = "reviewResult", value = "复查结果", required = true)
    private String reviewResult;
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
