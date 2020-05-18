package com.dico.modules.domain;

import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 菜单实体
 *
 * @author Gaodl
 * @version v1.0
 * 文件名称: SysMenu
 * 创建时间: 2018/12/20
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_menu")
@ApiModel(value = "菜单实体")
public class CurrentSysMenu {
    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String path;
    @Column(name = "icon")
    private String icon;
    private String areaId;
    @Transient
    private List<CurrentSysMenu> children;
}
