package com.dico.modules.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 部门领导人表
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
@Table(name = "sys_organization_user")
public class SysOrganizationUser {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    private String id;
    @ManyToOne(targetEntity=Organization.class)//体现依赖的主表
    @JoinColumn(name="organization_id",referencedColumnName="id")
    private Organization organization;
    @ManyToOne(targetEntity=SysUser.class)//体现依赖的主表
    @JoinColumn(name="user_id",referencedColumnName="id")
    private SysUser sysUser;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
    private boolean delFlag;
}
