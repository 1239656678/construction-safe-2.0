package com.dico.modules.domain;

import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
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
 * 消息接收人实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: NoticePerson
 * 创建时间: 2018/12/28
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "消息接收人实体")
@Table(name = "notice_person")
public class NoticePerson {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(value = "ID", name = "主键", required = false)
    private String id;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 32)
    @ApiModelProperty(value = "messageId", name = "消息ID", required = true)
    private String messageId;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 32)
    @ApiModelProperty(value = "userId", name = "用户ID", required = true)
    private String userId;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 5)
    @ApiModelProperty(value = "userName", name = "用户名称", required = true)
    private String userName;

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

    @ApiModelProperty(value = "readFlag", name = "阅读标识", hidden = true, required = false)
    private boolean readFlag;

    @Transient
    @JsonIgnoreProperties
    @ApiModelProperty(name = "noticeMessage", value = "通知内容")
    private NoticeMessage noticeMessage;
}
