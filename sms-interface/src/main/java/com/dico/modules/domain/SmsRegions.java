package com.dico.modules.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@ApiModel(value = "区域信息实体")
@Table(name = "sms_regions", schema = "construction_safe", catalog = "")
public class SmsRegions {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    @ApiModelProperty(name = "name", value = "区域名称", required = true)
    private String name;
    @ApiModelProperty(name = "pid", value = "上级区域ID")
    private String pid;
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
    private List<SmsRegions> childrens;
}
