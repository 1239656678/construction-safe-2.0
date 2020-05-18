package com.dico.modules.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@ApiModel(value = "用户计划详细列表实体")
@Table(name = "sms_user_inspection_plan", schema = "construction_safe", catalog = "")
public class SmsUserInspectionPlan {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    @ApiModelProperty(name = "cycle", value = "周期")
    private String cycle;
    @JsonBackReference
    @JSONField(serialize = false)
    @JoinColumn(name = "inspection_id")
    @ApiModelProperty(name = "inspectionId", value = "检查计划ID")
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private SmsInspectionPlan smsInspectionPlan;
    @ApiModelProperty(name = "status", value = "执行状态（0待巡检1巡检中2已完成3逾期）")
    private int status;
    @ApiModelProperty(name = "personLiableId", value = "巡检人")
    private String personLiableId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "beginDate", value = "开始时间", hidden = true)
    private Date beginDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "endDate", value = "结束时间", hidden = true)
    private Date endDate;
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
}
