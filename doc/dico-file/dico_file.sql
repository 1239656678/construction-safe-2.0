/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/4/26 10:07:58                           */
/*==============================================================*/


drop table if exists ATTACHMENTS;

/*==============================================================*/
/* Table: ATTACHMENTS                                           */
/*==============================================================*/
create table ATTACHMENTS
(
   ID                   varchar(32) not null comment '主键',
   FILE_CODE            varchar(64) comment '文件编码',
   FILE_NAME            varchar(100) comment '文件名称',
   FILE_TYPE            varchar(20) comment '文件类型',
   FILE_URL             varchar(500) comment '文件地址',
   TARGET_ID            varchar(32) comment '外键',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_USER_NAME     varchar(5) comment '创建人名称',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_USER_NAME     varchar(5) comment '修改人名称',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table ATTACHMENTS comment '附件表';

