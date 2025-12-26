-- 网络模拟系统数据库初始化脚本
-- 数据库：network_simulation
-- 字符集：utf8mb4
-- 存储引擎：InnoDB

-- 创建数据库
CREATE DATABASE IF NOT EXISTS network_simulation DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE network_simulation;

-- =====================================================
-- 1. 网络节点表 (network_node)
-- =====================================================
CREATE TABLE `network_node` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `node_id` VARCHAR(50) NOT NULL COMMENT '节点标识符',
  `node_name` VARCHAR(100) NOT NULL COMMENT '节点名称',
  `ip_address` VARCHAR(50) NOT NULL COMMENT 'IP地址',
  `mac_address` VARCHAR(50) NOT NULL COMMENT 'MAC地址',
  `position_x` DECIMAL(10,2) DEFAULT 0 COMMENT 'X坐标',
  `position_y` DECIMAL(10,2) DEFAULT 0 COMMENT 'Y坐标',
  `node_type` VARCHAR(20) NOT NULL DEFAULT 'ROUTER' COMMENT '节点类型',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_node_id` (`node_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网络节点表';

-- =====================================================
-- 2. 网络链路表 (network_link)
-- =====================================================
CREATE TABLE `network_link` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `link_id` VARCHAR(50) NOT NULL COMMENT '链路标识符',
  `source_node_id` VARCHAR(50) NOT NULL COMMENT '源节点ID',
  `target_node_id` VARCHAR(50) NOT NULL COMMENT '目标节点ID',
  `weight` INT NOT NULL DEFAULT 1 COMMENT '链路权重',
  `bandwidth` INT DEFAULT 1000 COMMENT '带宽(Mbps)',
  `delay` INT DEFAULT 1 COMMENT '延迟(ms)',
  `packet_loss_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '丢包率',
  `link_type` VARCHAR(20) NOT NULL DEFAULT 'ETHERNET' COMMENT '链路类型',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_link_id` (`link_id`),
  INDEX `idx_source_target` (`source_node_id`, `target_node_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网络链路表';

-- =====================================================
-- 3. 路由表 (routing_table)
-- =====================================================
CREATE TABLE `routing_table` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `node_id` VARCHAR(50) NOT NULL COMMENT '当前节点ID',
  `destination_network` VARCHAR(50) NOT NULL COMMENT '目标网络',
  `next_hop` VARCHAR(50) NOT NULL COMMENT '下一跳节点ID',
  `metric` INT NOT NULL DEFAULT 0 COMMENT '路由度量值',
  `interface` VARCHAR(50) COMMENT '出接口',
  `protocol` VARCHAR(20) NOT NULL DEFAULT 'STATIC' COMMENT '路由协议',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_node_dest` (`node_id`, `destination_network`),
  INDEX `idx_protocol` (`protocol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='路由表';

-- =====================================================
-- 4. 数据包传输记录表 (packet_transmission)
-- =====================================================
CREATE TABLE `packet_transmission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `transmission_id` VARCHAR(50) NOT NULL COMMENT '传输ID',
  `source_node_id` VARCHAR(50) NOT NULL COMMENT '源节点ID',
  `dest_node_id` VARCHAR(50) NOT NULL COMMENT '目标节点ID',
  `protocol` VARCHAR(20) NOT NULL DEFAULT 'TCP' COMMENT '传输协议',
  `data_content` TEXT COMMENT '数据内容',
  `data_size` INT NOT NULL DEFAULT 0 COMMENT '数据大小',
  `routing_algorithm` VARCHAR(20) NOT NULL DEFAULT 'DIJKSTRA' COMMENT '路由算法',
  `routing_path` VARCHAR(500) COMMENT '路由路径',
  `hop_count` INT NOT NULL DEFAULT 0 COMMENT '跳数',
  `total_delay` INT DEFAULT 0 COMMENT '总延迟',
  `status` VARCHAR(20) NOT NULL DEFAULT 'SUCCESS' COMMENT '传输状态',
  `error_message` VARCHAR(500) COMMENT '错误信息',
  `start_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` DATETIME COMMENT '结束时间',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transmission_id` (`transmission_id`),
  INDEX `idx_source_dest` (`source_node_id`, `dest_node_id`),
  INDEX `idx_start_time` (`start_time`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据包传输记录表';

-- =====================================================
-- 5. 数据包跳转记录表 (packet_hop)
-- =====================================================
CREATE TABLE `packet_hop` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `transmission_id` VARCHAR(50) NOT NULL COMMENT '传输ID',
  `hop_sequence` INT NOT NULL DEFAULT 1 COMMENT '跳序号',
  `current_node_id` VARCHAR(50) NOT NULL COMMENT '当前节点ID',
  `next_node_id` VARCHAR(50) COMMENT '下一跳节点ID',
  `action` VARCHAR(50) NOT NULL DEFAULT 'FORWARD' COMMENT '动作',
  `ttl` INT NOT NULL DEFAULT 64 COMMENT 'TTL值',
  `delay` INT DEFAULT 0 COMMENT '本跳延迟',
  `encapsulation_data` JSON COMMENT '封装数据详情',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_transmission_seq` (`transmission_id`, `hop_sequence`),
  INDEX `idx_current_node` (`current_node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据包跳转记录表';

-- =====================================================
-- 6. 协议封装层表 (protocol_layer)
-- =====================================================
CREATE TABLE `protocol_layer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `transmission_id` VARCHAR(50) NOT NULL COMMENT '传输ID',
  `hop_id` BIGINT COMMENT '跳记录ID',
  `layer_number` INT NOT NULL DEFAULT 1 COMMENT '层号',
  `layer_name` VARCHAR(50) NOT NULL COMMENT '层名称',
  `header_data` JSON COMMENT '头部数据',
  `payload_size` INT NOT NULL DEFAULT 0 COMMENT '负载大小',
  `total_size` INT NOT NULL DEFAULT 0 COMMENT '总大小',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_transmission_layer` (`transmission_id`, `layer_number`),
  INDEX `idx_hop_id` (`hop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='协议封装层表';

-- =====================================================
-- 7. 系统配置表 (system_config)
-- =====================================================
CREATE TABLE `system_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` VARCHAR(500) COMMENT '配置值',
  `config_type` VARCHAR(20) NOT NULL DEFAULT 'STRING' COMMENT '配置类型',
  `description` VARCHAR(200) COMMENT '描述',
  `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';
