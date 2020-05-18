package com.dico.modules.domain;

import com.dico.annotation.FilterBean;
import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import com.dico.annotation.ViewField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 数据字典类型实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: DataDictionaryType
 * 创建时间: 2018/12/27
 */
@Data
@Entity
@FilterBean
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "数据字典类型实体")
@Table(name = "data_dictionary_type")
public class DataDictionaryType {

    @Id
    @ViewField
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID", name = "主键", required = false)
    private String id;

    @ViewField
    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 20)
    @ApiModelProperty(value = "name", name = "类型名称", required = true)
    private String name;

    @ApiModelProperty(value = "createUser", name = "创建人", hidden = true, required = false)
    private String createUser;

    @ViewField
    @ApiModelProperty(value = "createUserName", name = "创建人名称", hidden = true, required = false)
    private String createUserName;

    @ViewField
    @ApiModelProperty(value = "createDate", name = "创建时间", hidden = true, required = false)
    private Date createDate;

    @ApiModelProperty(value = "updateUser", name = "修改人", hidden = true, required = false)
    private String updateUser;

    @ViewField
    @ApiModelProperty(value = "updateUserName", name = "修改人名称", hidden = true, required = false)
    private String updateUserName;

    @ViewField
    @ApiModelProperty(value = "updateDate", name = "修改时间", hidden = true, required = false)
    private Date updateDate;

    @ApiModelProperty(value = "delFlag", name = "删除标识", hidden = true, required = false)
    private boolean delFlag;

}
