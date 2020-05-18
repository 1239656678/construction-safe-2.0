package com.dico.feign.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "菜单实体")
public class CurrentSysMenu {
    private String id;
    private String name;
    private String path;
    private String icon;
    private String areaId;
    private List<CurrentSysMenu> children;
}
