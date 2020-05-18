package com.dico.modules.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Data
@Entity
public class RepairStatistice {
    @Id
    private String datetime;
    private int dangerNum;
    private int repairNum;
}
