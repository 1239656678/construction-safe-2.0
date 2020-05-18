package com.dico.modules.domain;

import com.dico.annotation.FilterBean;
import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import com.dico.annotation.ViewField;
import com.dico.enums.DeviceEnum;
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
 * 附件实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: Attachments
 * 创建时间: 2019/1/15
 */
@Data
@Entity
@FilterBean
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attachments")
@ApiModel(value = "附件实体")
public class Attachments {

    @Id
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(value = "ID", name = "主键", required = false)
    private String id;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 64)
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "fileCode", name = "文件编码", required = true)
    private String fileCode;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 100)
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "fileName", name = "文件名称", required = true)
    private String fileName;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 20)
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "fileType", name = "文件类型", required = true)
    private String fileType;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 500)
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "fileUrl", name = "文件地址", required = true)
    private String fileUrl;

    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "targetId", name = "引用ID", required = true)
    private String targetId;

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
}
