package com.dico.modules.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WillInspectionDTO {
    @Id
    private String statusId;
    private String equipmentId;
    private String equipmentClassId;
    private String equipmentCode;
    private String equipmentName;
    private String installRegions;
    private String person;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;
}
