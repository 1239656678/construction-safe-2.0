package com.dico.modules.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class DangerStatistice {
    @Id
    private String datetime;
    private int dangerNum;
    private int repairNum;
    private int reviewNum;
}
