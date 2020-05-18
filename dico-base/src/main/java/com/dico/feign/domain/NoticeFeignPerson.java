package com.dico.feign.domain;

import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 外部微服务调用通知人员实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: NoticePerson
 * 创建时间: 2019/1/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "微服务调用通知人员实体")
public class NoticeFeignPerson implements Serializable {

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 32)
    @ApiModelProperty(value = "id", name = "用户ID", required = true)
    private String id;

    @ValidatedNotEmpty
    @ValidatedSize(min = 1, max = 5)
    @ApiModelProperty(value = "name", name = "用户姓名", required = true)
    private String name;
}
