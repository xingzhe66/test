DROP TABLE IF EXISTS `mq_producer_msg`;
CREATE TABLE `mq_producer_msg`  (
                                  `mq_msg_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息id',
                                  `flow_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程id',
                                  `broker_name` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'broker名称',
                                  `offset_msg_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息发送成功，broker生成id',
                                  `msg_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息发送成功，producer生成id',
                                  `message` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息内容',
                                  `create_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
                                  `last_update` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后一次更新时间',
                                  `status` int(11) NULL DEFAULT NULL COMMENT '状态:1-消息建立；2-待发送；3-发送成功；4-异常',
                                  `message_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息类型',
                                  `seq_no` int(11) NULL DEFAULT NULL COMMENT '消息序列号',
                                  `queue_id` int(11) NULL DEFAULT NULL,
                                  PRIMARY KEY (`mq_msg_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'MQ生产者信息消息表(UPRIGHT)';
DROP TABLE IF EXISTS `mq_consumer_msg`;
CREATE TABLE `mq_consumer_msg`  (
                                  `mq_message_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息id',
                                  `msg_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接收消息id,与生产者id一致',
                                  `born_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产时间',
                                  `born_host` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产者host',
                                  `store_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储在broker时间',
                                  `store_host` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'broker的host',
                                  `topic` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息主题',
                                  `tag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息标签',
                                  `queue_id` int(11) NULL DEFAULT NULL COMMENT '队列id',
                                  `receive_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产者接收时间',
                                  `status` int(11) NULL DEFAULT NULL COMMENT '消息状态：1-接收成功，3:消费成功，4:消费失败',
                                  `update_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后一次更新状态时间',
                                  `remark` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                                  PRIMARY KEY (`mq_message_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'MQ消费者信息消息表(UPRIGHT)';

/*Data for the table `mq_consumer_msg` */

DROP TABLE IF EXISTS `mq_consumer_repeat`;
CREATE TABLE `mq_consumer_repeat`  (
                                     `mq_message_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息id',
                                     `msg_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接收消息id,与生产者id一致',
                                     `born_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产时间',
                                     `born_host` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产者host',
                                     `store_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储在broker时间',
                                     `store_host` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'broker的host',
                                     `topic` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息主题',
                                     `tag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息标签',
                                     `queue_id` int(11) NULL DEFAULT NULL COMMENT '队列id',
                                     `receive_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产者接收时间',
                                     `remark` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'MQ消费者信息消息表(UPRIGHT)';

/*Data for the table `mq_producer_msg` */