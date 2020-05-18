package com.dico.modules.domain;

import com.dico.annotation.FilterBean;
import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import com.dico.annotation.ViewField;
import com.dico.enums.DeviceEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysUser
 * 创建时间: 2018/12/27
 */
@Data
@Entity
@FilterBean
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_user")
@ApiModel(value = "用户实体")
/**
 * 如果使用feign要返回实体，则需要加上此注解 @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SysUser {
    @Id
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(value = "ID", name = "主键", required = false)
    private String id;

    @ValidatedNotEmpty
    @ValidatedSize(min = 6, max = 20)
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "username", name = "用户名", required = true)
    private String username;

    @ValidatedNotEmpty
    @ValidatedSize(min = 6, max = 64)
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "password", name = "用户密码", required = true)
    private String password;

    @ValidatedNotEmpty
    @ValidatedSize(min = 2, max = 5)
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "name", name = "姓名", required = true)
    private String name;

    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "imgHref", name = "用户头像", required = false)
    private String imgHref;

    @ValidatedNotEmpty
    @ValidatedSize(min = 2, max = 64)
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "position", name = "职位", required = true)
    private String position;

    @ValidatedNotEmpty
    @ValidatedSize(min = 2, max = 5)
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "post", name = "岗位", required = true)
    private String post;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 32)
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "organizationId", name = "所在部门ID", required = true)
    private String organizationId;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 30)
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "organizationName", name = "所在部门名称", required = true)
    private String organizationName;

    @ApiModelProperty(value = "salt", name = "盐", hidden = true)
    private String salt;

    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "enable", name = "禁用标识", required = false)
    private boolean enable;

    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "phoneNum", name = "手机号", required = false)
    private String phoneNum;

    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "temporary", name = "是否系统用户", required = true)
    private boolean temporary;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "allowBeginLoginDate", name = "临时用户可登录开始时间", required = false)
    private Date allowBeginLoginDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "allowEndLoginDate", name = "临时用户可登录结束时间", required = false)
    private Date allowEndLoginDate;

    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "createUser", name = "创建人", hidden = true, required = false)
    private String createUser;

    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "createDate", name = "创建时间", hidden = true, required = false)
    private Date createDate;

    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "updateUser", name = "修改人", hidden = true, required = false)
    private String updateUser;

    @ViewField(targets = {DeviceEnum.WEB, DeviceEnum.MOBILE, DeviceEnum.TABLE})
    @ApiModelProperty(value = "updateDate", name = "修改时间", hidden = true, required = false)
    private Date updateDate;

    @ApiModelProperty(value = "delFlag", name = "删除标识", hidden = true, required = false)
    private boolean delFlag;

    @Transient
    @ApiModelProperty(name = "attachmentId", value = "附件ID")
    private String attachmentId;

}
