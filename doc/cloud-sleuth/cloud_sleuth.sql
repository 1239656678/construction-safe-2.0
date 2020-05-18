/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50556
Source Host           : localhost:3306
Source Database       : zipkin

Target Server Type    : MYSQL
Target Server Version : 50556
File Encoding         : 65001

Date: 2019-01-09 16:48:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `zipkin_annotations`
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_annotations`;
CREATE TABLE `zipkin_annotations` (
  `trace_id_high` bigint(20) NOT NULL DEFAULT '0' COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.trace_id',
  `span_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.id',
  `a_key` varchar(255) NOT NULL COMMENT 'BinaryAnnotation.key or annotation.value if type == -1',
  `a_value` blob COMMENT 'BinaryAnnotation.value(), which must be smaller than 64KB',
  `a_type` int(11) NOT NULL COMMENT 'BinaryAnnotation.type() or -1 if annotation',
  `a_timestamp` bigint(20) DEFAULT NULL COMMENT 'Used to implement TTL; annotation.timestamp or zipkin_spans.timestamp',
  `endpoint_ipv4` int(11) DEFAULT NULL COMMENT 'Null when Binary/annotation.endpoint is null',
  `endpoint_ipv6` binary(16) DEFAULT NULL COMMENT 'Null when Binary/annotation.endpoint is null, or no IPv6 address',
  `endpoint_port` smallint(6) DEFAULT NULL COMMENT 'Null when Binary/annotation.endpoint is null',
  `endpoint_service_name` varchar(255) DEFAULT NULL COMMENT 'Null when Binary/annotation.endpoint is null',
  UNIQUE KEY `trace_id_high` (`trace_id_high`,`trace_id`,`span_id`,`a_key`,`a_timestamp`) COMMENT 'Ignore insert on duplicate',
  KEY `trace_id_high_2` (`trace_id_high`,`trace_id`,`span_id`) COMMENT 'for joining with zipkin_spans',
  KEY `trace_id_high_3` (`trace_id_high`,`trace_id`) COMMENT 'for getTraces/ByIds',
  KEY `endpoint_service_name` (`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames',
  KEY `a_type` (`a_type`) COMMENT 'for getTraces',
  KEY `a_key` (`a_key`) COMMENT 'for getTraces'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

-- ----------------------------
-- Records of zipkin_annotations
-- ----------------------------
INSERT INTO `zipkin_annotations` VALUES ('0', '7354432431779381388', '7354432431779381388', 'sr', null, '-1', '1547023291729115', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '7354432431779381388', '7354432431779381388', 'ss', null, '-1', '1547023293397572', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '7354432431779381388', '7354432431779381388', 'ca', 0x01, '0', '1547023291729115', '-1062731741', null, '-5193', '');
INSERT INTO `zipkin_annotations` VALUES ('0', '7354432431779381388', '7354432431779381388', 'http.method', 0x474554, '6', '1547023291729115', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '7354432431779381388', '7354432431779381388', 'http.path', 0x2F6E6F746963652F6461746150616765, '6', '1547023291729115', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '7354432431779381388', '7354432431779381388', 'mvc.controller.class', 0x4E6F746963654D657373616765436F6E74726F6C6C6572, '6', '1547023291729115', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '7354432431779381388', '7354432431779381388', 'mvc.controller.method', 0x6461746150616765, '6', '1547023291729115', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-2931920853637356015', '-2931920853637356015', 'sr', null, '-1', '1547023399126088', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-2931920853637356015', '-2931920853637356015', 'ss', null, '-1', '1547023399186661', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-2931920853637356015', '-2931920853637356015', 'ca', 0x01, '0', '1547023399126088', '-1062731741', null, '-4933', '');
INSERT INTO `zipkin_annotations` VALUES ('0', '-2931920853637356015', '-2931920853637356015', 'http.method', 0x504F5354, '6', '1547023399126088', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-2931920853637356015', '-2931920853637356015', 'http.path', 0x2F617574682F6C6F67696E, '6', '1547023399126088', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-2931920853637356015', '-2931920853637356015', 'mvc.controller.class', 0x4C6F67696E436F6E74726F6C6C6572, '6', '1547023399126088', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-2931920853637356015', '-2931920853637356015', 'mvc.controller.method', 0x6C6F67696E, '6', '1547023399126088', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '-5099581391415926559', 'cs', null, '-1', '1547023420989195', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '1159383391400233705', 'lc', '', '6', '1547023420694153', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'sr', null, '-1', '1547023420485101', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '-5099581391415926559', 'cr', null, '-1', '1547023421015796', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-8023064910435877182', 'cs', null, '-1', '1547023424687423', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'ss', null, '-1', '1547023422715768', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'sr', null, '-1', '1547023424952869', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-7521165222122743919', 'lc', '', '6', '1547023425199711', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '-5099581391415926559', 'http.method', 0x474554, '6', '1547023420989195', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'ss', null, '-1', '1547023424956243', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'ca', 0x01, '0', '1547023420485101', null, 0x00000000000000000000000000000001, '-4894', '');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'sr', null, '-1', '1547023428076353', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'cs', null, '-1', '1547023419694350', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'sr', null, '-1', '1547023424483080', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-8023064910435877182', 'cr', null, '-1', '1547023424956098', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'error', 0x526561642074696D6564206F7574, '6', '0', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '-5099581391415926559', 'http.path', 0x2F75736572466567696E5365727665722F66696E644F6E65, '6', '1547023420989195', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'ca', 0x01, '0', '1547023424952869', null, 0x00000000000000000000000000000001, '-4891', '');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'http.method', 0x504F5354, '6', '1547023420485101', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'ss', null, '-1', '1547023428031460', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-8023064910435877182', 'http.method', 0x474554, '6', '1547023424687423', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'ss', null, '-1', '1547023428080054', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'cr', null, '-1', '1547023420696125', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'http.path', 0x2F6E6F74696365466567696E5365727665722F736176654E6F74696365, '6', '1547023420485101', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'ca', 0x01, '0', '1547023424483080', null, 0x00000000000000000000000000000001, '-4884', '');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'http.method', 0x474554, '6', '1547023424952869', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '1299436476151758253', 'cs', null, '-1', '1547023425212176', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-8023064910435877182', 'http.path', 0x2F75736572466567696E5365727665722F66696E644F6E65, '6', '1547023424687423', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'error', 0x526561642074696D6564206F7574, '6', '1547023419694350', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'ca', 0x01, '0', '1547023428076353', null, 0x00000000000000000000000000000001, '-4891', '');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'http.method', 0x504F5354, '6', '1547023424483080', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'mvc.controller.class', 0x4E6F74696365466567696E536572766572, '6', '1547023420485101', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'http.path', 0x2F75736572466567696E5365727665722F66696E644F6E65, '6', '1547023424952869', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'http.method', 0x474554, '6', '1547023428076353', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'http.method', 0x504F5354, '6', '1547023419694350', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '1299436476151758253', 'cr', null, '-1', '1547023425212190', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '3688417689571505044', 'sr', null, '-1', '1547023427848087', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'mvc.controller.method', 0x736176654E6F74696365, '6', '1547023420485101', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'http.path', 0x2F6E6F74696365466567696E5365727665722F736176654E6F74696365, '6', '1547023424483080', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'mvc.controller.class', 0x55736572466567696E536572766572, '6', '1547023424952869', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'sr', null, '-1', '1547023428050083', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '1299436476151758253', 'error', 0x526561642074696D6564206F7574, '6', '1547023425212176', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'http.path', 0x2F75736572466567696E5365727665722F66696E644F6E65, '6', '1547023428076353', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'http.path', 0x2F6E6F74696365466567696E5365727665722F736176654E6F74696365, '6', '1547023419694350', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '3688417689571505044', 'ss', null, '-1', '1547023428562619', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'mvc.controller.class', 0x4E6F74696365466567696E536572766572, '6', '1547023424483080', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'mvc.controller.method', 0x66696E644F6E65, '6', '1547023424952869', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'ss', null, '-1', '1547023428561676', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '1299436476151758253', 'http.method', 0x504F5354, '6', '1547023425212176', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'mvc.controller.class', 0x55736572466567696E536572766572, '6', '1547023428076353', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '3688417689571505044', 'ca', 0x01, '0', '1547023427848087', null, 0x00000000000000000000000000000001, '-4933', '');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '696824503951692509', 'mvc.controller.method', 0x736176654E6F74696365, '6', '1547023424483080', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'ca', 0x01, '0', '1547023428050083', null, 0x00000000000000000000000000000001, '-4874', '');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'mvc.controller.method', 0x66696E644F6E65, '6', '1547023428076353', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '8641758906032953358', 'cs', null, '-1', '1547023420696335', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '1299436476151758253', 'http.path', 0x2F6E6F74696365466567696E5365727665722F736176654E6F74696365, '6', '1547023425212176', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '3688417689571505044', 'http.method', 0x504F5354, '6', '1547023427848087', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'http.method', 0x504F5354, '6', '1547023428050083', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '8641758906032953358', 'cr', null, '-1', '1547023420696351', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '3688417689571505044', 'http.path', 0x2F757365722F746573744E6F74696365, '6', '1547023427848087', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'http.path', 0x2F6E6F74696365466567696E5365727665722F736176654E6F74696365, '6', '1547023428050083', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '8641758906032953358', 'error', 0x526561642074696D6564206F7574, '6', '1547023420696335', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '3688417689571505044', 'mvc.controller.class', 0x53797355736572436F6E74726F6C6C6572, '6', '1547023427848087', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-2925548990283571888', 'cs', null, '-1', '1547023428053600', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-1774987999480480898', 'sr', null, '-1', '1547023423959066', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'mvc.controller.class', 0x4E6F74696365466567696E536572766572, '6', '1547023428050083', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '3688417689571505044', 'mvc.controller.method', 0x736176654E6F74696365, '6', '1547023427848087', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-2925548990283571888', 'cr', null, '-1', '1547023428080858', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '8641758906032953358', 'http.method', 0x504F5354, '6', '1547023420696335', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-1774987999480480898', 'ss', null, '-1', '1547023425213548', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'mvc.controller.method', 0x736176654E6F74696365, '6', '1547023428050083', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-1774987999480480898', 'ca', 0x01, '0', '1547023423959066', null, 0x00000000000000000000000000000001, '-4933', '');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-2925548990283571888', 'http.method', 0x474554, '6', '1547023428053600', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '8641758906032953358', 'http.path', 0x2F6E6F74696365466567696E5365727665722F736176654E6F74696365, '6', '1547023420696335', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-1774987999480480898', 'http.method', 0x504F5354, '6', '1547023423959066', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '3688417689571505044', '-2925548990283571888', 'http.path', 0x2F75736572466567696E5365727665722F66696E644F6E65, '6', '1547023428053600', '-1062706431', null, null, 'dico-notice-message');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '4722645084479103985', 'sr', null, '-1', '1547023419692078', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-1774987999480480898', 'http.path', 0x2F757365722F746573744E6F74696365, '6', '1547023423959066', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-1774987999480480898', 'mvc.controller.class', 0x53797355736572436F6E74726F6C6C6572, '6', '1547023423959066', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '4722645084479103985', 'ss', null, '-1', '1547023420697387', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '-1774987999480480898', '-1774987999480480898', 'mvc.controller.method', 0x736176654E6F74696365, '6', '1547023423959066', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '4722645084479103985', 'ca', 0x01, '0', '1547023419692078', null, 0x00000000000000000000000000000001, '-4933', '');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '4722645084479103985', 'http.method', 0x504F5354, '6', '1547023419692078', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '4722645084479103985', 'http.path', 0x2F757365722F746573744E6F74696365, '6', '1547023419692078', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '4722645084479103985', 'mvc.controller.class', 0x53797355736572436F6E74726F6C6C6572, '6', '1547023419692078', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '4722645084479103985', 'mvc.controller.method', 0x736176654E6F74696365, '6', '1547023419692078', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'sr', null, '-1', '1547023420998084', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'ss', null, '-1', '1547023421013667', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'ca', 0x01, '0', '1547023420998084', null, 0x00000000000000000000000000000001, '-4891', '');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'http.method', 0x474554, '6', '1547023420998084', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'http.path', 0x2F75736572466567696E5365727665722F66696E644F6E65, '6', '1547023420998084', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'mvc.controller.class', 0x55736572466567696E536572766572, '6', '1547023420998084', '-1062706431', null, null, 'dico-system');
INSERT INTO `zipkin_annotations` VALUES ('0', '4722645084479103985', '7929941251163192836', 'mvc.controller.method', 0x66696E644F6E65, '6', '1547023420998084', '-1062706431', null, null, 'dico-system');

-- ----------------------------
-- Table structure for `zipkin_dependencies`
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_dependencies`;
CREATE TABLE `zipkin_dependencies` (
  `day` date NOT NULL,
  `parent` varchar(255) NOT NULL,
  `child` varchar(255) NOT NULL,
  `call_count` bigint(20) DEFAULT NULL,
  UNIQUE KEY `day` (`day`,`parent`,`child`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

-- ----------------------------
-- Records of zipkin_dependencies
-- ----------------------------

-- ----------------------------
-- Table structure for `zipkin_spans`
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_spans`;
CREATE TABLE `zipkin_spans` (
  `trace_id_high` bigint(20) NOT NULL DEFAULT '0' COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `debug` bit(1) DEFAULT NULL,
  `start_ts` bigint(20) DEFAULT NULL COMMENT 'Span.timestamp(): epoch micros used for endTs query and to implement TTL',
  `duration` bigint(20) DEFAULT NULL COMMENT 'Span.duration(): micros used for minDuration and maxDuration query',
  UNIQUE KEY `trace_id_high` (`trace_id_high`,`trace_id`,`id`) COMMENT 'ignore insert on duplicate',
  KEY `trace_id_high_2` (`trace_id_high`,`trace_id`,`id`) COMMENT 'for joining with zipkin_annotations',
  KEY `trace_id_high_3` (`trace_id_high`,`trace_id`) COMMENT 'for getTracesByIds',
  KEY `name` (`name`) COMMENT 'for getTraces and getSpanNames',
  KEY `start_ts` (`start_ts`) COMMENT 'for getTraces ordering and range'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

-- ----------------------------
-- Records of zipkin_spans
-- ----------------------------
INSERT INTO `zipkin_spans` VALUES ('0', '-2931920853637356015', '-2931920853637356015', 'post /auth/login', null, null, '1547023399126088', '60573');
INSERT INTO `zipkin_spans` VALUES ('0', '-1774987999480480898', '-8023064910435877182', 'get', '696824503951692509', null, '1547023424687423', '268675');
INSERT INTO `zipkin_spans` VALUES ('0', '-1774987999480480898', '-7521165222122743919', 'hystrix', '-1774987999480480898', null, '1547023425199711', '12175');
INSERT INTO `zipkin_spans` VALUES ('0', '-1774987999480480898', '-1774987999480480898', 'post /user/testnotice', null, null, '1547023423959066', '1254482');
INSERT INTO `zipkin_spans` VALUES ('0', '-1774987999480480898', '696824503951692509', 'post /noticefeignserver/savenotice', '-1774987999480480898', null, '1547023424952869', null);
INSERT INTO `zipkin_spans` VALUES ('0', '-1774987999480480898', '1299436476151758253', 'post', '-1774987999480480898', null, '1547023425212176', '14');
INSERT INTO `zipkin_spans` VALUES ('0', '3688417689571505044', '-4369367707997565259', 'post /noticefeignserver/savenotice', '3688417689571505044', null, '1547023428076353', null);
INSERT INTO `zipkin_spans` VALUES ('0', '3688417689571505044', '-2925548990283571888', 'get', '-4369367707997565259', null, '1547023428053600', '27258');
INSERT INTO `zipkin_spans` VALUES ('0', '3688417689571505044', '3688417689571505044', 'post /user/testnotice', null, null, '1547023427848087', '714532');
INSERT INTO `zipkin_spans` VALUES ('0', '4722645084479103985', '-5099581391415926559', 'get', '7929941251163192836', null, '1547023420989195', '26601');
INSERT INTO `zipkin_spans` VALUES ('0', '4722645084479103985', '1159383391400233705', 'hystrix', '4722645084479103985', null, '1547023420694153', '1306');
INSERT INTO `zipkin_spans` VALUES ('0', '4722645084479103985', '4722645084479103985', 'post /user/testnotice', null, null, '1547023419692078', '1005309');
INSERT INTO `zipkin_spans` VALUES ('0', '4722645084479103985', '7929941251163192836', 'get /userFeignServer/findone', '4722645084479103985', null, '1547023419694350', '1001775');
INSERT INTO `zipkin_spans` VALUES ('0', '4722645084479103985', '8641758906032953358', 'post', '4722645084479103985', null, '1547023420696335', '16');
INSERT INTO `zipkin_spans` VALUES ('0', '7354432431779381388', '7354432431779381388', 'get /notice/datapage', null, null, '1547023291729115', '1668457');
