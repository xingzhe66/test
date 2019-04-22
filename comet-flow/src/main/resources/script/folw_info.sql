/*Table structure for table `flow_info` */

DROP TABLE IF EXISTS `flow_info`;

CREATE TABLE `flow_info` (
  `flow_id` varchar(36) NOT NULL COMMENT '流程id',
  `flow_class_name` varchar(50) DEFAULT NULL COMMENT '流程类名称',
  `start_time` varchar(20) DEFAULT NULL COMMENT '流程开始时间',
  `end_time` varchar(20) DEFAULT NULL COMMENT '流程结束时间',
  `flow_status` int(11) DEFAULT NULL COMMENT '流程状态：1-开始，2-结束，3-异常',
  `flow_in` varchar(2000) DEFAULT NULL COMMENT '流程入参',
  `flow_out` varchar(2000) DEFAULT NULL COMMENT '流程返回结果',
  PRIMARY KEY (`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程信息表(UPRIGHT)';

/*Data for the table `flow_info` */
