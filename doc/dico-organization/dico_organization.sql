/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/4/26 10:08:32                           */
/*==============================================================*/


drop table if exists COMPANY;

drop table if exists ORGANIZATION;

/*==============================================================*/
/* Table: COMPANY                                               */
/*==============================================================*/
create table COMPANY
(
   ID                   varchar(32) not null,
   ICON                 varchar(200) comment 'LOGO',
   CODE                 varchar(50) comment '编码',
   NAME                 varchar(30) comment '名称',
   DETAILS              varchar(1000) comment '描述',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_USER_NAME     varchar(5) comment '创建人名称',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_USER_NAME     varchar(5) comment '修改人名称',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table COMPANY comment '单位表';

/*==============================================================*/
/* Table: ORGANIZATION                                          */
/*==============================================================*/
create table ORGANIZATION
(
   ID                   varchar(32) not null,
   COMPANY_ID           varchar(32) comment '所属单位',
   PARENT_ORGANIZATION_ID varchar(32) comment '上级组织ID',
   NAME                 varchar(30) comment '名称',
   DETAILS              varchar(1000) comment '描述',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_USER_NAME     varchar(5) comment '创建人名称',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_USER_NAME     varchar(5) comment '修改人名称',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table ORGANIZATION comment '组织机构表';

