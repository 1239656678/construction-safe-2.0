package com.dico.modules.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class SmsMaintenancePlanDto {
    @Id
    private String id;
    private String equipmentCode;
    private String equipmentName;
    private String className;
    private int cycle;
    private String maintenanceOrganization;
    private String maintenanceUser;
    private Date beginDate;
    private Date endDate;
    private String remark;
}
