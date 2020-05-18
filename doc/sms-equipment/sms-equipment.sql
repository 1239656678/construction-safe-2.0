/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/4/11 17:12:21                           */
/*==============================================================*/


drop table if exists SMS_EQUIPMENT_BASE_INFO;

drop table if exists SMS_EQUIPMENT_CLASS;

drop table if exists SMS_EQUIPMENT_DIY_COLUMNS;

drop table if exists SMS_PROJECT_AREA;

/*==============================================================*/
/* Table: SMS_EQUIPMENT_BASE_INFO                               */
/*==============================================================*/
create table SMS_EQUIPMENT_BASE_INFO
(
   ID                   varchar(32) not null comment 'ID',
   EQUIPMENT_CODE       varchar(50) comment '设备编号',
   EQUIPMENT_NAME       varchar(50) comment '设备名称',
   COMPANY_ID           varchar(32) comment '所属单位ID',
   COMPANY_NAME         varchar(30) comment '所属单位名称',
   ORGANIZATION_ID      varchar(32) comment '归属部门ID',
   ORGANIZATION_NAME    varchar(50) comment '归属部门名称',
   EQUIPMENT_PERSON_LIABLE_ID varchar(32) comment '设备责任人ID',
   EQUIPMENT_PERSON_LIABLE_NAME varchar(32) comment '设备责任人名称',
   EQUIPMENT_TYPE       varchar(32) comment '设备分类',
   EQUIPMENT_MODEL      varchar(20) comment '设备型号',
   EQUIPMENT_STATUS     varchar(32) comment '设备状态',
   INSTALL_AREA         varchar(32) comment '安装区域',
   EQUIPMENT_MANUFACTURER varchar(50) comment '厂家',
   EQUIPMENT_MANUFACTURE_TIME datetime comment '生产日期',
   NEXT_REPAIR_TIME     datetime comment '下次维修时间',
   SCRAP_TIME           datetime comment '报废日期',
   QR_CODE              varchar(200) comment '二维码',
   REMARK               varchar(1000) comment '备注',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_EQUIPMENT_BASE_INFO comment '设备基础信息表';

/*==============================================================*/
/* Table: SMS_EQUIPMENT_CLASS                                   */
/*==============================================================*/
create table SMS_EQUIPMENT_CLASS
(
   ID                   varchar(32) not null comment 'ID',
   CLASS_NAME           varchar(50) comment '类型名称',
   PARENT_CLASS         varchar(32) comment '上级类型',
   COMPANY_ID           varchar(32) comment '所属单位ID',
   COMPANY_NAME         varchar(30) comment '所属单位名称',
   REMARK               varchar(1000) comment '备注',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_EQUIPMENT_CLASS comment '设备类型表';

/*==============================================================*/
/* Table: SMS_EQUIPMENT_DIY_COLUMNS                             */
/*==============================================================*/
create table SMS_EQUIPMENT_DIY_COLUMNS
(
   ID                   varchar(32) not null comment 'ID',
   EQUIPMENT            varchar(32) comment '设备ID',
   COLUMNS_NAME         varchar(20) comment '属性名称',
   COLUMNS_VALUE        varchar(50) comment '属性值',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_EQUIPMENT_DIY_COLUMNS comment '设备自定义属性表';

/*==============================================================*/
/* Table: SMS_PROJECT_AREA                                      */
/*==============================================================*/
create table SMS_PROJECT_AREA
(
   ID                   varchar(32) not null comment 'ID',
   PROJECT_CODE         varchar(50) comment '项目编号',
   PROJECT_NAME         varchar(50) comment '项目名称',
   COMPANY_ID           varchar(32) comment '所属单位ID',
   COMPANY_NAME         varchar(30) comment '所属单位名称',
   LIABLE_ORGANIZATION_ID varchar(32) comment '责任部门ID',
   LIABLE_ORGANIZATION_NAME varchar(30) comment '责任部门名称',
   SECURITY_PERSON_LIABLE_ID varchar(32) comment '安全责任人ID',
   SECURITY_PERSON_LIABLE_NAME varchar(5) comment '安全责任人名称',
   PERSON_LIABLE_TELEPHONE varchar(11) comment '责任人联系电话',
   PROJECT_STATUS       char(1) comment '项目状态',
   PROJECT_FINISH_DATE  datetime comment '项目上线时间',
   PARENT_PROJECT       varchar(32) comment '上级项目',
   REMARK               varchar(1000) comment '备注',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SMS_PROJECT_AREA comment '项目区域表';

