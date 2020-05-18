package com.dico.modules.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class SmsMaintenanceRecordDto {
    @Id
    private String id;
    private String requipmentCode;
    private String requipmentName;
    private String className;
    private Integer status;
    private String maintenanceUser;
    private String remark;
    private String maintenanceOrganization;
    private Integer cycle;
    private String cost;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date finishDate;
}
