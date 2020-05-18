package com.dico.modules.dto;

import com.dico.modules.domain.SmsEquipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SmsEquipmentFaultWillReviewDTO {
    @Id
    private String faultInfoId;

    private String faultRecordId;

    private Integer staus;

    @Transient
    private SmsEquipment equipment;
    private String faultUser;

    private String reportUser;//报修人

    private Date reportDate;//报修时间

    private String remark;//故障描述

    @Transient
    private Object files;
}
