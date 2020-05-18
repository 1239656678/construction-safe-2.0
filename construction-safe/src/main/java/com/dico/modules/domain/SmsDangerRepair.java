package com.dico.modules.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Data
@Entity
@ApiModel(value = "隐患整改实体")
@Table(name = "sms_danger_repair", schema = "construction_safe", catalog = "")
public class SmsDangerRepair {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    @ApiModelProperty(name = "dangerId", value = "隐患ID", required = true)
    private String dangerId;
    @ApiModelProperty(name = "repairOrganizationId", value = "整改部门ID", required = true)
    private String repairOrganizationId;
    @ApiModelProperty(name = "repairUserId", value = "整改人ID", required = true)
    private String repairUserId;
    @ApiModelProperty(name = "repairOpinion", value = "整改要求", required = true)
    private String repairOpinion;
    @ApiModelProperty(name = "repairLimit", value = "整改期限", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date repairLimit;
    @ApiModelProperty(name = "repairStatus", value = "整改状态(0待整改1已完成)", required = true)
    private boolean repairStatus;
    @ApiModelProperty(name = "repairResult", value = "整改结果", required = true)
    private String repairResult;
    @ApiModelProperty(name = "hasCost", value = "是否需要成本", required = true)
    private boolean hasCost;
    @ApiModelProperty(name = "activitiStatus", value = "流程状态（0未发起1办理中2拒绝3通过）", readOnly = true)
    private int activitiStatus;
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
    private String dangerRemark;

    @Transient
    private Object files;

    @Transient
    private boolean changeDangerStatus;
}
