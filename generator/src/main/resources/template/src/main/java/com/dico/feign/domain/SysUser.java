package com.dico.feign.domain;

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
    private Date allowBeginLoginDate;
    private Date allowEndLoginDate;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
    private boolean delFlag;
}
