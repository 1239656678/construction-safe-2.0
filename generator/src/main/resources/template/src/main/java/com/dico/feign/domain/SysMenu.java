package com.dico.feign.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 菜单实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysMenu
 * 创建时间: 2018/12/20
 */
@Data
@ApiModel(value = "菜单实体")
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu {
    @ApiModelProperty(value = "ID", name = "主键", required = false)
    private String id;

    @ApiModelProperty(value = "name", name = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "address", name = "链接地址", required = true)
    private String address;

    @ApiModelProperty(value = "icon", name = "图片", required = true)
    private String icon;

    @ApiModelProperty(value = "level", name = "菜单级别", required = true)
    private String level;

    @ApiModelProperty(value = "parent", name = "父菜单", required = false)
    private String parent;

    @ApiModelProperty(value = "areaId", name = "区域ID", required = true)
    private String areaId;

    @ApiModelProperty(value = "createUser", name = "创建人", hidden = true, required = false)
    private String createUser;

    @ApiModelProperty(value = "createDate", name = "创建时间", hidden = true, required = false)
    private Date createDate;

    @ApiModelProperty(value = "updateUser", name = "修改人", hidden = true, required = false)
    private String updateUser;

    @ApiModelProperty(value = "updateDate", name = "修改时间", hidden = true, required = false)
    private Date updateDate;

    @ApiModelProperty(value = "delFlag", name = "删除标识", hidden = true, required = false)
    private boolean delFlag;
}
