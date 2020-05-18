package com.dico.modules.domain;

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
 * 用户角色关联关系
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysUserRole
 * 创建时间: 2018/12/20
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_user_role")
public class SysUserRole {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    private String id;
    private String userId;
    private String roleId;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
    private boolean delFlag;
}
