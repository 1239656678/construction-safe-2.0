package com.dico.feign.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachments {
    private String id;
    private String fileCode;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private String targetId;
}
