package com.dico.modules.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Administrator
 * @date 2020-05-11 10:27
 */
@Data
@Entity
public class SmsEquipmentFaultInfoReviewDto {
    @Id
    private String recordId;
    private String reviewUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

}
