package com.dico.modules.dto;

import com.dico.modules.domain.SmsEquipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MaintainWillReviewDTO {
    @Id
    private String mainId;

    private String planId;
    @Transient
    private SmsEquipment equipment;
    private String maintainCreateUser;
    @Transient
    private Object files;
}
