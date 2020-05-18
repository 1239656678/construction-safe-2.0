package com.dico.modules.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-03-31 13:52
 */
@Data
@Entity
@ApiModel(value = "设备分类关联保养标准实体")
@Table(name = "sms_class_maintenance_target")
public class SmsClassMaintenanceTarget {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    @ApiModelProperty(name = "equipmentClassId", value = "设备分类ID")
    private String equipmentClassId;
    @ApiModelProperty(name = "targetId", value = "保养标准ID")
    private String targetId;
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
