package com.dico.modules.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WillReviewDTO {
    @Id
    private String reviewId;
    private String equipmentCode;
    private String equipmentName;
    private String dangerLevel;
    private String dangerAddress;
    private String reviewUser;
    private String repairResult;
}
