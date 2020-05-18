package com.dico.feign.domain;

import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 外部微服务调用通知消息实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: NoticefeignDomain
 * 创建时间: 2019/1/8
 */
@Data
@ApiModel(value = "通知消息实体")
@NoArgsConstructor
@AllArgsConstructor
public class NoticeFeignDomain implements Serializable {

    @ValidatedNotEmpty
    @ApiModelProperty(value = "noticePersonList", name = "通知人员集合", required = true)
    private List<NoticeFeignPerson> noticeFeignPersonList;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 50)
    @ApiModelProperty(value = "noticeTitle", name = "通知消息标题", required = true)
    private String noticeTitle;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 5000)
    @ApiModelProperty(value = "noticeContent", name = "通知消息内容", required = true)
    private String noticeContent;
}
