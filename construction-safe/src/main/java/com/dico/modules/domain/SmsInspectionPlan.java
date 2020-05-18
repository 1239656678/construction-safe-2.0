package com.dico.modules.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dico.annotation.FilterBean;
import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import com.dico.annotation.ViewField;
import com.dico.enums.DeviceEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
import java.util.List;
import java.util.Objects;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "巡检计划实体")

@Table(name = "sms_inspection_plan")
public class SmsInspectionPlan {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "ID", value = "主键")
    private String id;
    @ApiModelProperty(name = "planCode", value = "计划编号", required = true)
    private String planCode;

    @ApiModelProperty(name = "planName", value = "计划名称", required = true)
    private String planName;

    @ApiModelProperty(name = "planType", value = "计划类型0日常检查1周检查2月度检查3季度检查4专项检查", required = true)
    private int planType;

    @ApiModelProperty(name = "organizationId", value = "责任部门ID", required = true)
    private String organizationId;

    @Transient
    @ApiModelProperty(name = "organizationName", value = "责任部门名称")
    private String organizationName;

    @ApiModelProperty(name = "personLiableId", value = "责任人ID", required = true)
    private String personLiableId;

    @Transient
    @ApiModelProperty(name = "personLiableName", value = "责任人名称")
    private String personLiableName;

    @ValidatedNotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(name = "beginDate", value = "计划开始时间", required = true, example = "yyyy-MM-dd")
    private Date beginDate;

    @ValidatedNotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(name = "endDate", value = "计划结束时间", required = true, example = "yyyy-MM-dd")
    private Date endDate;

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
    @ApiModelProperty(name = "itemIds", value = "绑定的ID字符串集合", example = "['ID','ID']")
    private String[] itemIds;

    @Transient
    private List<SmsUserInspectionPlan> smsUserInspectionPlanList;
}
