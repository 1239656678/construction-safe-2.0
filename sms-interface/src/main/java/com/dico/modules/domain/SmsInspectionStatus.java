package com.dico.modules.domain;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
@ApiModel(value = "计划执行状态实体")
@Table(name = "sms_inspection_status")
public class SmsInspectionStatus {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "ID", value = "主键")
    private String id;

    @ApiModelProperty(name = "planId", value = "详细计划ID")
    private String userInspectionPlanId;

    @ApiModelProperty(name = "equipmentClassId", value = "设备分类ID")
    private String equipmentClassId;

    @ApiModelProperty(name = "equipmentId", value = "设备ID")
    private String equipmentId;

    @ApiModelProperty(name = "status", value = "状态（0待巡检2已完成3逾期）")
    private int status;

    @ApiModelProperty(name = "instructions", value = "检查说明")
    private String instructions;

    @JSONField(serialize = false)
    @ApiModelProperty(name = "result", value = "检查结果集")
    private String result;

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
    @ApiModelProperty(name = "resultList", value = "检查结果集")
    private List<Map<String, Object>> resultList;
}
