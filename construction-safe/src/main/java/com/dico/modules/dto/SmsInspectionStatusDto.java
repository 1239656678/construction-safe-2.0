package com.dico.modules.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class SmsInspectionStatusDto {
    @Id
    private String equipmentCode;
    private String equipmentName;
    private String equipmentClass;
    private String installArea;
    private String inspectionStatus;
}
