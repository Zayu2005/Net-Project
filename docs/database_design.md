# 数据库设计文档

## 数据库：network_simulation

---

## 1. 网络节点表 (network_node)

存储网络拓扑中的所有节点信息

| 字段名 | 类型 | 长度 | 允许空 | 主键 | 默认值 | 说明 |
|--------|------|------|--------|------|--------|------|
| id | BIGINT | - | NO | YES | AUTO_INCREMENT | 主键ID |
| node_id | VARCHAR | 50 | NO | - | - | 节点标识符(如A,B,C) |
| node_name | VARCHAR | 100 | NO | - | - | 节点名称 |
| ip_address | VARCHAR | 50 | NO | - | - | IP地址 |
| mac_address | VARCHAR | 50 | NO | - | - | MAC地址 |
| position_x | DECIMAL | 10,2 | YES | - | 0 | X坐标位置 |
| position_y | DECIMAL | 10,2 | YES | - | 0 | Y坐标位置 |
| node_type | VARCHAR | 20 | NO | - | ROUTER | 节点类型(ROUTER/SWITCH/HOST) |
| status | TINYINT | - | NO | - | 1 | 状态(0-离线,1-在线) |
| created_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 创建时间 |
| updated_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 更新时间 |

**索引：**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_node_id` (`node_id`)
- INDEX `idx_status` (`status`)

**建表SQL：**
```sql
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
```

---

## 2. 网络链路表 (network_link)

存储节点之间的连接关系和链路属性

| 字段名 | 类型 | 长度 | 允许空 | 主键 | 默认值 | 说明 |
|--------|------|------|--------|------|--------|------|
| id | BIGINT | - | NO | YES | AUTO_INCREMENT | 主键ID |
| link_id | VARCHAR | 50 | NO | - | - | 链路标识符 |
| source_node_id | VARCHAR | 50 | NO | - | - | 源节点ID |
| target_node_id | VARCHAR | 50 | NO | - | - | 目标节点ID |
| weight | INT | - | NO | - | 1 | 链路权重/成本 |
| bandwidth | INT | - | YES | - | 1000 | 带宽(Mbps) |
| delay | INT | - | YES | - | 1 | 延迟(ms) |
| packet_loss_rate | DECIMAL | 5,2 | YES | - | 0.00 | 丢包率(%) |
| link_type | VARCHAR | 20 | NO | - | ETHERNET | 链路类型 |
| status | TINYINT | - | NO | - | 1 | 状态(0-断开,1-连接) |
| created_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 创建时间 |
| updated_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 更新时间 |

**索引：**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_link_id` (`link_id`)
- INDEX `idx_source_target` (`source_node_id`, `target_node_id`)
- INDEX `idx_status` (`status`)

**建表SQL：**
```sql
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
```

---

## 3. 路由表 (routing_table)

存储每个节点的路由信息

| 字段名 | 类型 | 长度 | 允许空 | 主键 | 默认值 | 说明 |
|--------|------|------|--------|------|--------|------|
| id | BIGINT | - | NO | YES | AUTO_INCREMENT | 主键ID |
| node_id | VARCHAR | 50 | NO | - | - | 当前节点ID |
| destination_network | VARCHAR | 50 | NO | - | - | 目标网络 |
| next_hop | VARCHAR | 50 | NO | - | - | 下一跳节点ID |
| metric | INT | - | NO | - | 0 | 路由度量值 |
| interface | VARCHAR | 50 | YES | - | - | 出接口 |
| protocol | VARCHAR | 20 | NO | - | STATIC | 路由协议(STATIC/OSPF/RIP/BGP) |
| created_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 创建时间 |
| updated_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 更新时间 |

**索引：**
- PRIMARY KEY (`id`)
- INDEX `idx_node_dest` (`node_id`, `destination_network`)
- INDEX `idx_protocol` (`protocol`)

**建表SQL：**
```sql
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
```

---

## 4. 数据包传输记录表 (packet_transmission)

记录每次数据包传输的详细信息

| 字段名 | 类型 | 长度 | 允许空 | 主键 | 默认值 | 说明 |
|--------|------|------|--------|------|--------|------|
| id | BIGINT | - | NO | YES | AUTO_INCREMENT | 主键ID |
| transmission_id | VARCHAR | 50 | NO | - | - | 传输ID(UUID) |
| source_node_id | VARCHAR | 50 | NO | - | - | 源节点ID |
| dest_node_id | VARCHAR | 50 | NO | - | - | 目标节点ID |
| protocol | VARCHAR | 20 | NO | - | TCP | 传输协议 |
| data_content | TEXT | - | YES | - | - | 数据内容 |
| data_size | INT | - | NO | - | 0 | 数据大小(字节) |
| routing_algorithm | VARCHAR | 20 | NO | - | DIJKSTRA | 路由算法 |
| routing_path | VARCHAR | 500 | YES | - | - | 路由路径(JSON数组) |
| hop_count | INT | - | NO | - | 0 | 跳数 |
| total_delay | INT | - | YES | - | 0 | 总延迟(ms) |
| status | VARCHAR | 20 | NO | - | SUCCESS | 传输状态(SUCCESS/FAILED/TIMEOUT) |
| error_message | VARCHAR | 500 | YES | - | - | 错误信息 |
| start_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 开始时间 |
| end_time | DATETIME | - | YES | - | - | 结束时间 |
| created_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_transmission_id` (`transmission_id`)
- INDEX `idx_source_dest` (`source_node_id`, `dest_node_id`)
- INDEX `idx_start_time` (`start_time`)
- INDEX `idx_status` (`status`)

**建表SQL：**
```sql
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
```

---

## 5. 数据包跳转记录表 (packet_hop)

记录数据包在每一跳的详细信息

| 字段名 | 类型 | 长度 | 允许空 | 主键 | 默认值 | 说明 |
|--------|------|------|--------|------|--------|------|
| id | BIGINT | - | NO | YES | AUTO_INCREMENT | 主键ID |
| transmission_id | VARCHAR | 50 | NO | - | - | 传输ID(关联packet_transmission) |
| hop_sequence | INT | - | NO | - | 1 | 跳序号 |
| current_node_id | VARCHAR | 50 | NO | - | - | 当前节点ID |
| next_node_id | VARCHAR | 50 | YES | - | - | 下一跳节点ID |
| action | VARCHAR | 50 | NO | - | FORWARD | 动作(FORWARD/DELIVER/DROP) |
| ttl | INT | - | NO | - | 64 | TTL值 |
| delay | INT | - | YES | - | 0 | 本跳延迟(ms) |
| encapsulation_data | JSON | - | YES | - | - | 封装数据详情(JSON) |
| created_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- PRIMARY KEY (`id`)
- INDEX `idx_transmission_seq` (`transmission_id`, `hop_sequence`)
- INDEX `idx_current_node` (`current_node_id`)

**建表SQL：**
```sql
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
```

---

## 6. 协议封装层表 (protocol_layer)

存储数据包在每一层的封装信息

| 字段名 | 类型 | 长度 | 允许空 | 主键 | 默认值 | 说明 |
|--------|------|------|--------|------|--------|------|
| id | BIGINT | - | NO | YES | AUTO_INCREMENT | 主键ID |
| transmission_id | VARCHAR | 50 | NO | - | - | 传输ID |
| hop_id | BIGINT | - | YES | - | - | 跳记录ID(关联packet_hop) |
| layer_number | INT | - | NO | - | 1 | 层号(1-7) |
| layer_name | VARCHAR | 50 | NO | - | - | 层名称 |
| header_data | JSON | - | YES | - | - | 头部数据(JSON) |
| payload_size | INT | - | NO | - | 0 | 负载大小 |
| total_size | INT | - | NO | - | 0 | 总大小 |
| created_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 创建时间 |

**索引：**
- PRIMARY KEY (`id`)
- INDEX `idx_transmission_layer` (`transmission_id`, `layer_number`)
- INDEX `idx_hop_id` (`hop_id`)

**建表SQL：**
```sql
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
```

---

## 7. 系统配置表 (system_config)

存储系统配置参数

| 字段名 | 类型 | 长度 | 允许空 | 主键 | 默认值 | 说明 |
|--------|------|------|--------|------|--------|------|
| id | BIGINT | - | NO | YES | AUTO_INCREMENT | 主键ID |
| config_key | VARCHAR | 100 | NO | - | - | 配置键 |
| config_value | VARCHAR | 500 | YES | - | - | 配置值 |
| config_type | VARCHAR | 20 | NO | - | STRING | 配置类型 |
| description | VARCHAR | 200 | YES | - | - | 描述 |
| created_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 创建时间 |
| updated_time | DATETIME | - | NO | - | CURRENT_TIMESTAMP | 更新时间 |

**索引：**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_config_key` (`config_key`)

**建表SQL：**
```sql
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
```

---

## 数据库初始化脚本

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS network_simulation DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE network_simulation;

-- 执行上述所有建表SQL

-- 插入初始节点数据
INSERT INTO network_node (node_id, node_name, ip_address, mac_address, position_x, position_y, node_type) VALUES
('A', '节点A', '192.168.1.1', 'AA:BB:CC:DD:EE:01', 100, 100, 'ROUTER'),
('B', '节点B', '192.168.1.2', 'AA:BB:CC:DD:EE:02', 300, 80, 'ROUTER'),
('C', '节点C', '192.168.1.3', 'AA:BB:CC:DD:EE:03', 500, 100, 'ROUTER'),
('D', '节点D', '192.168.1.4', 'AA:BB:CC:DD:EE:04', 400, 250, 'ROUTER'),
('E', '节点E', '192.168.1.5', 'AA:BB:CC:DD:EE:05', 200, 300, 'ROUTER'),
('F', '节点F', '192.168.1.6', 'AA:BB:CC:DD:EE:06', 100, 450, 'ROUTER');

-- 插入初始链路数据
INSERT INTO network_link (link_id, source_node_id, target_node_id, weight, bandwidth, delay) VALUES
('AB', 'A', 'B', 4, 1000, 2),
('AE', 'A', 'E', 2, 1000, 1),
('BC', 'B', 'C', 3, 1000, 1),
('BD', 'B', 'D', 5, 1000, 3),
('CD', 'C', 'D', 2, 1000, 1),
('DE', 'D', 'E', 3, 1000, 2),
('EF', 'E', 'F', 4, 1000, 2),
('DF', 'D', 'F', 6, 1000, 3);

-- 插入系统配置
INSERT INTO system_config (config_key, config_value, config_type, description) VALUES
('default_routing_algorithm', 'DIJKSTRA', 'STRING', '默认路由算法'),
('max_ttl', '64', 'INTEGER', '最大TTL值'),
('packet_timeout', '5000', 'INTEGER', '数据包超时时间(ms)'),
('enable_animation', 'true', 'BOOLEAN', '是否启用动画效果');
```

---

## ER图说明

```
network_node (1) ----< (*) network_link
network_node (1) ----< (*) routing_table
network_node (1) ----< (*) packet_transmission
packet_transmission (1) ----< (*) packet_hop
packet_hop (1) ----< (*) protocol_layer
```

---

## 备注

1. 使用MySQL 8.0+
2. 字符集：utf8mb4
3. 存储引擎：InnoDB
4. 所有时间字段使用DATETIME类型
5. JSON字段用于存储复杂数据结构（如封装详情）
6. 建议开启慢查询日志，监控性能
