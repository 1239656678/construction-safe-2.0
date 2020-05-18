package com.dico.feign.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {
    private String id;
    private String username;
    private String name;
    private String imgHref;
    private String organizationId;
    private String organizationName;
    private boolean enable;
    private String phoneNum;
    private boolean isTemporary;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date allowBeginLoginDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date allowEndLoginDate;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
    private boolean delFlag;
}
