/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : comet-mq

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-04-11 09:46:13
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `flow_info`
-- ----------------------------
DROP TABLE IF EXISTS `flow_info`;
CREATE TABLE `flow_info` (
  `flow_id` varchar(255) DEFAULT NULL COMMENT '流程id',
  `start_time` datetime DEFAULT NULL COMMENT '流程开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '流程结束时间',
  `flow_in` longblob COMMENT '入参'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flow_info
-- ----------------------------

-- ----------------------------
-- Table structure for `product_msg`
-- ----------------------------
DROP TABLE IF EXISTS `product_msg`;
CREATE TABLE `product_msg` (
  `producted_msg_id` varchar(20) NOT NULL COMMENT '消息id',
  `flow_id` varchar(255) DEFAULT NULL COMMENT '流程id',
  `message` longblob COMMENT '消息内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update` datetime DEFAULT NULL COMMENT '最后一次更新时间',
  `status` int(11) DEFAULT NULL COMMENT '状态:1-消息建立；2-待发送；3-发送成功；4-异常',
  `message_type` varchar(10) DEFAULT NULL COMMENT '消息类型',
  PRIMARY KEY (`producted_msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product_msg
-- ----------------------------
