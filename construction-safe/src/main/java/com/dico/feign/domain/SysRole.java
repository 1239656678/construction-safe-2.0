package com.dico.feign.domain;

import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
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

/**
 * 角色实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysRole
 * 创建时间: 2018/12/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "角色实体")
public class SysRole {
    @ApiModelProperty(value = "ID", name = "主键")
    private String id;
    @ApiModelProperty(value = "code", name = "编码", required = true)
    private String code;
    @ApiModelProperty(value = "name", name = "名称", required = true)
    private String name;
    @ApiModelProperty(value = "createUser", name = "创建人", hidden = true)
    private String createUser;
    @ApiModelProperty(value = "createDate", name = "创建时间", hidden = true)
    private Date createDate;
    @ApiModelProperty(value = "updateUser", name = "修改人", hidden = true)
    private String updateUser;
    @ApiModelProperty(value = "updateDate", name = "修改时间", hidden = true)
    private Date updateDate;
    @ApiModelProperty(value = "delFlag", name = "删除标识", hidden = true)
    private boolean delFlag;

}
