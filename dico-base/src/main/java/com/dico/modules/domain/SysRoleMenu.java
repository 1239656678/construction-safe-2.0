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
 * 角色菜单关联关系
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysRoleMenu
 * 创建时间: 2018/12/20
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_role_menu")
public class SysRoleMenu {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    private String id;
    private String roleId;
    private String menuId;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
    private boolean delFlag;
}
