/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/4/26 10:07:42                           */
/*==============================================================*/


drop table if exists DATA_DICTIONARY;

drop table if exists DATA_DICTIONARY_TYPE;

/*==============================================================*/
/* Table: DATA_DICTIONARY                                       */
/*==============================================================*/
create table DATA_DICTIONARY
(
   ID                   varchar(32) not null,
   TYPE_ID              varchar(32) comment '类型ID',
   NAME                 varchar(20) comment '名称',
   VALUE                varchar(20) comment '数据值',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_USER_NAME     varchar(5) comment '创建人名称',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_USER_NAME     varchar(5) comment '修改人名称',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table DATA_DICTIONARY comment '数据类型表';

/*==============================================================*/
/* Table: DATA_DICTIONARY_TYPE                                  */
/*==============================================================*/
create table DATA_DICTIONARY_TYPE
(
   ID                   varchar(32) not null,
   NAME                 varchar(20) comment '名称',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_USER_NAME     varchar(5) comment '创建人名称',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_USER_NAME     varchar(5) comment '修改人名称',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table DATA_DICTIONARY_TYPE comment '数据字典类型表';

