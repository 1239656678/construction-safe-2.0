package com.dico.modules.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * @author Administrator
 * @date 2020-04-29 10:37
 */
@Data
@Entity
public class EquipmentClassTreeDTO {
    @Id
    private String id;
    @Transient
    private Object equipmentClass;
    @Transient
    private Object equipmentList;



}
