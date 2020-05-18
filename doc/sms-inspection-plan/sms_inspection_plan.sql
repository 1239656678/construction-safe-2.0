/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/4/26 10:06:30                           */
/*==============================================================*/


drop table if exists SMS_EQUIPMENT_CLASS_TARGET;

drop table if exists SMS_INSPECTION_PLAN;

drop table if exists SMS_INSPECTION_TARGET;

drop table if exists SMS_PLAN_EQUIPMENT;

drop table if exists SMS_PLAN_EXAMINE_RECORD;

/*==============================================================*/
/* Table: SMS_EQUIPMENT_CLASS_TARGET                            */
/*==============================================================*/
create table SMS_EQUIPMENT_CLASS_TARGET
(
   ID                   varchar(32) not null comment 'ID',
   EQUIPMENT_CLASS_ID   varchar(32) comment '设备类型ID',
   TARGET_ID            varchar(32) comment '巡检项ID',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_EQUIPMENT_CLASS_TARGET comment '类型巡检项绑定表';

/*==============================================================*/
/* Table: SMS_INSPECTION_PLAN                                   */
/*==============================================================*/
create table SMS_INSPECTION_PLAN
(
   ID                   varchar(32) not null comment 'ID',
   PLAN_CODE            varchar(30) comment '计划编号',
   PLAN_NAME            varchar(30) comment '计划名称',
   EXAMINE_STATUS       char(1) comment '审批状态(0未提交1待审核2已通过)',
   COMPANY_ID           varchar(32) comment '所属单位ID',
   COMPANY_NAME         varchar(30) comment '所属单位名称',
   PLAN_STATUS          tinyint comment '计划状态',
   REMARK               varchar(1000) comment '描述',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_INSPECTION_PLAN comment '计划表';

/*==============================================================*/
/* Table: SMS_INSPECTION_TARGET                                 */
/*==============================================================*/
create table SMS_INSPECTION_TARGET
(
   ID                   varchar(32) not null comment 'ID',
   TARGET_CODE          varchar(20) comment '巡检项编号',
   TARGET_NAME          varchar(30) comment '巡检项名称',
   RUN_CYCLE_ID         varchar(32) comment '执行周期ID',
   RUN_CYCLE_NAME       varchar(10) comment '执行周期名称',
   COMPANY_ID           varchar(32) comment '所属单位ID',
   COMPANY_NAME         varchar(30) comment '所属单位名称',
   REMARK               varchar(1000) comment '描述',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_INSPECTION_TARGET comment '巡检项表';

/*==============================================================*/
/* Table: SMS_PLAN_EQUIPMENT                                    */
/*==============================================================*/
create table SMS_PLAN_EQUIPMENT
(
   ID                   varchar(32) not null comment 'ID',
   PLAN_ID              varchar(32) comment '计划ID',
   EQUIPMENT_ID         varchar(32) comment '设备ID',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_PLAN_EQUIPMENT comment '计划设备绑定表';

/*==============================================================*/
/* Table: SMS_PLAN_EXAMINE_RECORD                               */
/*==============================================================*/
create table SMS_PLAN_EXAMINE_RECORD
(
   ID                   varchar(32) not null comment 'ID',
   PLAN_ID              varchar(32) comment '计划ID',
   PLAN_NAME            varchar(30) comment '计划名称',
   EXAMINE_RESULT       char(1) comment '审批结果(0退回1通过)',
   REMARK               varchar(1000) comment '描述',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_PLAN_EXAMINE_RECORD comment '计划审核记录表';

