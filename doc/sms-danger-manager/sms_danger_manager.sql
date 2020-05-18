/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/4/26 10:09:04                           */
/*==============================================================*/


drop table if exists SMS_DANGER_INFO;

drop table if exists SMS_DANGER_REPAIR;

drop table if exists SMS_DANGER_REVIEW;

/*==============================================================*/
/* Table: SMS_DANGER_INFO                                       */
/*==============================================================*/
create table SMS_DANGER_INFO
(
   ID                   varchar(32) not null comment 'ID',
   DANGER_TYPE          varchar(32) comment '隐患类型',
   DANGER_ADDRESS       varchar(50) comment '隐患位置',
   DANGER_LEVEL_ID      varchar(32) comment '隐患等级(数据字典获取)',
   DANGER_LEVEL_NAME    varchar(5) comment '隐患等级名称',
   DANGER_STATUS        char(1) comment '隐患状态(0待整改1整改中2待复查3已完成)',
   REMARK               varchar(1000) comment '隐患描述',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_DANGER_INFO comment '隐患表';

/*==============================================================*/
/* Table: SMS_DANGER_REPAIR                                     */
/*==============================================================*/
create table SMS_DANGER_REPAIR
(
   ID                   varchar(32) not null comment 'ID',
   DANGER_ID            varchar(32) comment '隐患ID',
   REPAIR_ORGANIZATION_ID varchar(32) comment '整改部门ID',
   REPAIR_ORGANIZATION_NAME varchar(30) comment '整改部门名称',
   REPAIR_USER_ID       varchar(32) comment '整改人ID',
   REPAIR_USER_NAME     varchar(5) comment '整改人名称',
   REPAIR_OPINION       varchar(1000) comment '整改意见',
   REPAIR_STATUS        tinyint comment '整改状态(true已完成false整改中)',
   REPAIR_RESULT        varchar(1000) comment '整改结果',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_DANGER_REPAIR comment '隐患整改表';

/*==============================================================*/
/* Table: SMS_DANGER_REVIEW                                     */
/*==============================================================*/
create table SMS_DANGER_REVIEW
(
   ID                   varchar(32) not null comment 'ID',
   DANGER_ID            varchar(32) comment '隐患ID',
   REVIEW_ORGANIZATION_ID varchar(32) comment '复查部门ID',
   REVIEW_ORGANIZATION_NAME varchar(30) comment '复查部门名称',
   REVIEW_USER_ID       varchar(32) comment '复查人ID',
   REVIEW_USER_NAME     varchar(5) comment '复查人名称',
   REVIEW_RESULT        varchar(1000) comment '复查结果',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_DANGER_REVIEW comment '隐患复查表';

