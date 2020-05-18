package com.dico.modules.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 检查计划列表实体
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class InspectionPlanDTO {
    @Id
    private String id;
    private String code;
    private String name;
    private String inspectionUser;
    private String organizationName;
    private int type;
    @Transient
    private String typeName;
    private int status;
    @Transient
    private String statusName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
}
