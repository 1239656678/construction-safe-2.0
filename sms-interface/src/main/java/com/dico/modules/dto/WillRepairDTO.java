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
public class WillRepairDTO {
    @Id
    private String repairId;
    private String equipmentName;
    private String dangerLevel;
    private String dangerAddress;
    private String inspectionUser;
    private String repairOpinion;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date repairLimit;
}
