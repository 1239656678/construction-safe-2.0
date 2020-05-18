/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/4/26 10:08:18                           */
/*==============================================================*/


drop table if exists NOTICE_MESSAGE;

drop table if exists NOTICE_PERSON;

/*==============================================================*/
/* Table: NOTICE_MESSAGE                                        */
/*==============================================================*/
create table NOTICE_MESSAGE
(
   ID                   varchar(32) not null comment 'ID',
   TITLE                varchar(50) comment '标题',
   CONTENT              varchar(5000) comment '内容',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_USER_NAME     varchar(5) comment '创建人名称',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_USER_NAME     varchar(5) comment '修改人名称',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table NOTICE_MESSAGE comment '通知消息';

/*==============================================================*/
/* Table: NOTICE_PERSON                                         */
/*==============================================================*/
create table NOTICE_PERSON
(
   ID                   varchar(32) not null comment 'ID',
   MESSAGE_ID           varchar(32) comment '消息外键',
   USER_ID              varchar(32) comment '用户外键',
   READ_STATUS          tinyint comment '读取标识',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_USER_NAME     varchar(5) comment '创建人名称',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_USER_NAME     varchar(5) comment '修改人名称',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table NOTICE_PERSON comment '通知人员';

