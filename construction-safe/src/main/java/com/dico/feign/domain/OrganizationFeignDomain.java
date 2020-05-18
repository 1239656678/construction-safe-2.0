package com.dico.feign.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 * 供外部微服务调用组织机构实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: OrganizationFeignDomain
 * 创建时间: 2019/3/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationFeignDomain {
    private String id;
    private String name;
    private String details;
}
