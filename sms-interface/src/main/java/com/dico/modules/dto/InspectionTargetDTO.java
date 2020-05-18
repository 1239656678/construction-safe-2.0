package com.dico.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 检查计划列表实体
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class InspectionTargetDTO {
    @Id
    private String id;
    private String name;
    private boolean normal = true;
}
