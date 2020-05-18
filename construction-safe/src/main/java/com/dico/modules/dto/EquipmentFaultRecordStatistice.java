package com.dico.modules.dto;

import com.dico.modules.domain.SmsEquipmentFaultInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
public class EquipmentFaultRecordStatistice {
    @Id
    private String equipmentCode;
    private String equipmentName;
    private String installArea;
    private String equipmentType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date produceDate;//安装时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date scrappedDate;//报废日期
    private String equipmentModel;//设备型号
    private String responsibleOrganizationName;//责任部门
    private String responsibleUser;//责任人
    private String equipmentRemark;

    private String reportUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String reportDate;
    private String reportRemark;
    @Transient
    private Object reportFiles;

    private String FaultUserOrganizationName;
    private String faultUser;
    private String faultResult;
    @Transient
    private Object faultFiles;

}
