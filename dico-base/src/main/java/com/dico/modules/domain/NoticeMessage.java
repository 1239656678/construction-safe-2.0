package com.dico.modules.domain;

import com.dico.annotation.FilterBean;
import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import com.dico.annotation.ViewField;
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
 * 通知消息实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: NoticeMessage
 * 创建时间: 2018/12/28
 */
@Data
@Entity
@FilterBean
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "通知消息实体")
@Table(name = "notice_message")
public class NoticeMessage {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(value = "ID", name = "主键", required = false)
    private String id;

    @ViewField
    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 50)
    @ApiModelProperty(value = "title", name = "标题", required = true)
    private String title;

    @ViewField
    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 5000)
    @ApiModelProperty(value = "content", name = "内容", required = true)
    private String content;

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
    @JsonIgnoreProperties
    @ApiModelProperty(name = "receive", value = "接收人")
    private String receive;

}
