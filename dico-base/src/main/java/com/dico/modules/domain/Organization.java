package com.dico.modules.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 组织实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: Organization
 * 创建时间: 2019/2/26
 */
@Data
@Entity
@ApiModel(value = "组织实体")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORGANIZATION")
public class Organization {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(value = "ID", name = "主键", required = false)
    private String id;

    @ApiModelProperty(value = "parentOrganizationId", name = "上级组织ID", required = false)
    private String parentOrganizationId;

    @ApiModelProperty(value = "name", name = "组织名称", required = true)
    private String name;

    @ApiModelProperty(value = "details", name = "组织简介", required = true)
    private String details;

    @ApiModelProperty(value = "createUser", name = "创建人", hidden = true, required = false)
    private String createUser;

    @ApiModelProperty(value = "createUserName", name = "创建人名称", hidden = true, required = false)
    private String createUserName;

    @ApiModelProperty(value = "createDate", name = "创建时间", hidden = true, required = false)
    private Date createDate;

    @ApiModelProperty(value = "updateUser", name = "修改人", hidden = true, required = false)
    private String updateUser;

    @ApiModelProperty(value = "updateUserName", name = "修改人名称", hidden = true, required = false)
    private String updateUserName;

    @ApiModelProperty(value = "updateDate", name = "修改时间", hidden = true, required = false)
    private Date updateDate;

    @ApiModelProperty(value = "delFlag", name = "删除标识", hidden = true, required = false)
    private boolean delFlag;

    @Transient
    @ApiModelProperty(value = "children", name = "子组织", hidden = true)
    private List<Organization> children;

}
