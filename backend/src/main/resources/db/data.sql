-- 网络模拟系统初始数据脚本
USE network_simulation;

-- =====================================================
-- 插入初始节点数据 - 三层网络架构
-- 第一层(核心层 y=100): 路由器 (ROUTER)
-- 第二层(汇聚层 y=300): 交换机 (SWITCH)
-- 第三层(接入层 y=500): 主机 (HOST)
-- =====================================================
INSERT INTO network_node (node_id, node_name, ip_address, mac_address, position_x, position_y, node_type) VALUES
-- 核心层: 路由器 (y=100)
('A', '核心路由器A', '192.168.1.1', 'AA:BB:CC:DD:EE:01', 150, 100, 'ROUTER'),
('D', '核心路由器D', '192.168.1.4', 'AA:BB:CC:DD:EE:04', 350, 100, 'ROUTER'),
('G', '核心路由器G', '192.168.1.7', 'AA:BB:CC:DD:EE:07', 550, 100, 'ROUTER'),
('K', '核心路由器K', '192.168.1.11', 'AA:BB:CC:DD:EE:11', 750, 100, 'ROUTER'),
-- 汇聚层: 交换机 (y=300)
('B', '汇聚交换机B', '192.168.1.2', 'AA:BB:CC:DD:EE:02', 150, 300, 'SWITCH'),
('F', '汇聚交换机F', '192.168.1.6', 'AA:BB:CC:DD:EE:06', 350, 300, 'SWITCH'),
('I', '汇聚交换机I', '192.168.1.9', 'AA:BB:CC:DD:EE:09', 550, 300, 'SWITCH'),
('L', '汇聚交换机L', '192.168.1.12', 'AA:BB:CC:DD:EE:12', 750, 300, 'SWITCH'),
-- 接入层: 主机 (y=500)
('M', '主机M', '192.168.1.13', 'AA:BB:CC:DD:EE:13', 100, 500, 'HOST'),
('C', '主机C', '192.168.1.3', 'AA:BB:CC:DD:EE:03', 200, 500, 'HOST'),
('E', '主机E', '192.168.1.5', 'AA:BB:CC:DD:EE:05', 300, 500, 'HOST'),
('J', '主机J', '192.168.1.10', 'AA:BB:CC:DD:EE:10', 400, 500, 'HOST'),
('H', '主机H', '192.168.1.8', 'AA:BB:CC:DD:EE:08', 550, 500, 'HOST'),
('N', '主机N', '192.168.1.14', 'AA:BB:CC:DD:EE:14', 700, 500, 'HOST');

-- =====================================================
-- 插入初始链路数据 - 三层网络架构
-- 核心层互联 (ROUTER ↔ ROUTER)
-- 下行链路 (ROUTER ↔ SWITCH)
-- 接入链路 (SWITCH ↔ HOST)
-- =====================================================
INSERT INTO network_link (link_id, source_node_id, target_node_id, weight, bandwidth, delay) VALUES
-- 核心层互联 (ROUTER ↔ ROUTER) - 高带宽低延迟
('AD', 'A', 'D', 2, 10000, 1),
('DG', 'D', 'G', 2, 10000, 1),
('GK', 'G', 'K', 2, 10000, 1),
('AK', 'A', 'K', 3, 10000, 2),
-- 下行链路 (ROUTER ↔ SWITCH) - 中等带宽
('AB', 'A', 'B', 3, 1000, 2),
('DF', 'D', 'F', 3, 1000, 2),
('GI', 'G', 'I', 3, 1000, 2),
('KL', 'K', 'L', 3, 1000, 2),
-- 冗余下行链路 (提高可靠性)
('AF', 'A', 'F', 4, 1000, 3),
('DI', 'D', 'I', 4, 1000, 3),
-- 接入链路 (SWITCH ↔ HOST) - 标准带宽
('BM', 'B', 'M', 2, 100, 1),
('BC', 'B', 'C', 2, 100, 1),
('FE', 'F', 'E', 2, 100, 1),
('FJ', 'F', 'J', 2, 100, 1),
('IH', 'I', 'H', 2, 100, 1),
('IN', 'I', 'N', 2, 100, 1),
('LN', 'L', 'N', 2, 100, 1);

-- =====================================================
-- 插入系统配置参数
-- =====================================================
INSERT INTO system_config (config_key, config_value, config_type, description) VALUES
('default_routing_algorithm', 'DIJKSTRA', 'STRING', '默认路由算法'),
('max_ttl', '64', 'INTEGER', '最大TTL值'),
('packet_timeout', '5000', 'INTEGER', '数据包超时时间(ms)'),
('enable_animation', 'true', 'BOOLEAN', '是否启用动画效果');
