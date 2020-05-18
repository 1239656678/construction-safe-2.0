package com.dico.modules.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class SmsEquipmentDto {
    @Id
    private String id;
    private String equipmentCode;
    private String equipmentName;
    private String installArea;
    private String equipmentClass;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scrappedDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date produceDate;
    private String model;
    private String organizationLiable;
    private String personLiable;
    private String remark;
}
