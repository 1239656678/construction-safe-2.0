package com.dico.feign.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外部微服务调用通知人员实体
 *
 * @author Stephen
 * @version v1.0
 * 文件名称: NoticePerson
 * 创建时间: 2019/1/8
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeFeignPerson {
    private String id;
    private String name;
}
