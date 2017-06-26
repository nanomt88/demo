drop table  if EXISTS `t_pay`;
CREATE TABLE `t_pay` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20) DEFAULT NULL,
  `username` varchar(20) NOT NULL,
  `amount` decimal(20,2) DEFAULT '0.00' COMMENT '余额',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否同步， 0：未同步；1：已同步',
  `detail` varchar(100) DEFAULT NULL,
  `update_by` varchar(20) NOT NULL COMMENT '用户名',
  `create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `last_update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_pay_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table  if EXISTS `t_balance`;
CREATE TABLE `t_balance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `amount` decimal(20,2) DEFAULT '0.00' COMMENT '余额',
  `update_by` varchar(20) NOT NULL COMMENT '用户名',
  `create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `last_update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_balance_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table  if EXISTS t_event_consumer;
CREATE TABLE `t_event_consumer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '应用ID',
  `topic` varchar(100) NOT NULL COMMENT '消息topic',
  `msg_key` varchar(100) NOT NULL COMMENT '事件关键字',
  `msg_body` text NOT NULL COMMENT '消息内容',
  `msg_extra` text COMMENT '消息扩展内容',
  `create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间,时间戳',
  `last_update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_t_event_producer_t_m_k` (`topic`,`msg_key`),
  KEY `idx_t_event_producer_t_c_t` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='MQ消费者消费消息记录表';

drop table  if EXISTS t_event_consumer_task;
CREATE TABLE `t_event_consumer_task` (
  `topic` varchar(100) NOT NULL COMMENT '消息topic',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`topic`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消费者同步消息记录表';

drop table  if EXISTS t_event_producer_task;
CREATE TABLE `t_event_producer_task` (
  `topic` varchar(100) NOT NULL COMMENT '消息topic',
  `update_time` timestamp NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`topic`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消费者同步消息记录表';

drop table  if EXISTS t_event_producer;
CREATE TABLE `t_event_producer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '应用ID',
  `topic` varchar(100) NOT NULL COMMENT '消息topic',
  `msg_key` varchar(100) NOT NULL COMMENT '事件关键字',
  `msg_body` text NOT NULL COMMENT '消息内容',
  `msg_extra` text COMMENT '消息扩展参数',
  `status` tinyint(4) NOT NULL COMMENT '消息状态；0 ： PREPARED；1：SUBMITTED ； 2：ROLL_BACK',
  `create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间,时间戳',
  `last_update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_t_event_producer_t_m_k` (`topic`,`msg_key`),
  INDEX `idx_t_event_producer_c_t`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='MQ 生产者发送消息记录表';