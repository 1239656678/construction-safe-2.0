package com.dico.modules.dto;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Data
@Entity
public class ClassStatistice {
    @Id
    private String className;
    private Double num;
    @Transient
    private String statistice;
}
