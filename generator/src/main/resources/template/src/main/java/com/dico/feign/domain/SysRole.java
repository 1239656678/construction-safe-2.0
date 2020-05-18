package com.dico.feign.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class SysRole {
    private String id;
    private String code;
    private String name;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
    private boolean delFlag;
}
