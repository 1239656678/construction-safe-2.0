package com.dico.modules.domain;

import com.dico.annotation.ValidatedNotEmpty;
import com.fasterxml.jackson.annotation.JsonFormat;
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

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "故障维修结果实体")
@Table(name = "sms_equipment_fault_record")
public class SmsEquipmentFaultRecord {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "ID", value = "主键")
    private String id;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "faultInfoId", value = "故障ID")
    private String faultInfoId;

    @ValidatedNotEmpty
    @ApiModelProperty(name = "result", value = "维修结果")
    private String result;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    @ApiModelProperty(name = "staus", value = "状态（0:待复查，1：复查中，2：已完成）")
    private Integer staus;

    @ApiModelProperty(name = "REVIEW_USER_ID", value = "复查人", hidden = true)
    private String reviewUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "endDate", name = "复查期限", required = true)
    private Date endDate;
    @ApiModelProperty(name = "createUser", value = "创建人", hidden = true)
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "createDate", value = "创建时间", hidden = true)
    private Date createDate;

    @ApiModelProperty(name = "updateUser", value = "修改人", hidden = true)
    private String updateUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "updateDate", value = "修改时间", hidden = true)
    private Date updateDate;

    @ApiModelProperty(name = "delFlag", value = "删除标识", hidden = true)
    private boolean delFlag;
}
