/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/4/26 10:08:46                           */
/*==============================================================*/


drop table if exists SYS_MENU;

drop table if exists SYS_ROLE;

drop table if exists SYS_ROLE_MENU;

drop table if exists SYS_USER;

drop table if exists SYS_USER_ROLE;

/*==============================================================*/
/* Table: SYS_MENU                                              */
/*==============================================================*/
create table SYS_MENU
(
   ID                   varchar(32) not null,
   NAME                 varchar(10) comment '名称',
   ADDRESS              varchar(100) comment '地址',
   ICON                 varchar(30) comment '图标',
   LEVEL                char(1) comment '菜单级别',
   PARENT               varchar(32) comment '上级菜单',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SYS_MENU comment '菜单表';

/*==============================================================*/
/* Table: SYS_ROLE                                              */
/*==============================================================*/
create table SYS_ROLE
(
   ID                   varchar(32) not null,
   CODE                 varchar(10) comment '角色编码',
   NAME                 varchar(10) comment '角色名称',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SYS_ROLE comment '角色表';

/*==============================================================*/
/* Table: SYS_ROLE_MENU                                         */
/*==============================================================*/
create table SYS_ROLE_MENU
(
   ID                   varchar(32) not null,
   ROLE_ID              varchar(10) comment '角色ID',
   MENU_ID              varchar(10) comment '菜单ID',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SYS_ROLE_MENU comment '角色菜单表';

/*==============================================================*/
/* Table: SYS_USER                                              */
/*==============================================================*/
create table SYS_USER
(
   ID                   varchar(32) not null,
   USERNAME             varchar(10) comment '帐号',
   PASSWORD             varchar(64) comment '密码',
   NAME                 varchar(5) comment '姓名',
   IMG_HREF             varchar(200) comment '头像',
   SALT                 varchar(32) comment '盐',
   COMPANY_ID           varchar(32) comment '所在单位ID',
   COMPANY_NAME         varchar(30) comment '所在单位名称',
   ORGANZATION_ID       varchar(32) comment '部门ID',
   ORGANZATION_NAME     varchar(30) comment '部门名称',
   ENABLE               tinyint comment '禁用标识',
   IS_TEMPORARY         tinyint comment '是否临时用户',
   ALLOW_BEGIN_LOGIN_DATE datetime comment '临时用户可登录开始时间',
   ALLOW_END_LOGIN_DATE datetime comment '临时用户可登录结束时间',
   PHONE_NUM            varchar(11) comment '手机号码',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SYS_USER comment '用户表';

/*==============================================================*/
/* Table: SYS_USER_ROLE                                         */
/*==============================================================*/
create table SYS_USER_ROLE
(
   ID                   varchar(32) not null,
   USER_ID              varchar(32) comment '用户ID',
   ROLE_ID              varchar(32) comment '角色ID',
   CREATE_USER          varchar(32) comment '创建人',
   CREATE_DATE          datetime comment '创建时间',
   UPDATE_USER          varchar(32) comment '修改人',
   UPDATE_DATE          datetime comment '修改时间',
   DEL_FLAG             tinyint comment '删除标识',
   primary key (ID)
);

alter table SYS_USER_ROLE comment '用户角色表';

