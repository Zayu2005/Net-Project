# API接口文档

## 基础信息

- **项目名称**: 网络数据传输与封装模拟系统
- **版本**: v1.0.0
- **Base URL**: `http://localhost:8080/api/v1`
- **协议**: HTTP/HTTPS
- **数据格式**: JSON

---

## 通用返回格式

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1640000000000
}
```

### 失败响应
```json
{
  "code": 400,
  "message": "错误信息",
  "data": null,
  "timestamp": 1640000000000
}
```

### 状态码说明
| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 1. 网络节点管理

### 1.1 获取所有节点

**接口**: `GET /nodes`

**描述**: 获取所有网络节点列表

**请求参数**: 无

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "nodeId": "A",
      "nodeName": "节点A",
      "ipAddress": "192.168.1.1",
      "macAddress": "AA:BB:CC:DD:EE:01",
      "positionX": 100.0,
      "positionY": 100.0,
      "nodeType": "ROUTER",
      "status": 1,
      "createdTime": "2024-01-01 12:00:00",
      "updatedTime": "2024-01-01 12:00:00"
    }
  ]
}
```

---

### 1.2 获取单个节点详情

**接口**: `GET /nodes/{nodeId}`

**描述**: 根据节点ID获取节点详细信息

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nodeId | String | 是 | 节点ID（如A,B,C） |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "nodeId": "A",
    "nodeName": "节点A",
    "ipAddress": "192.168.1.1",
    "macAddress": "AA:BB:CC:DD:EE:01",
    "positionX": 100.0,
    "positionY": 100.0,
    "nodeType": "ROUTER",
    "status": 1,
    "routingTable": [
      {
        "destinationNetwork": "192.168.1.4",
        "nextHop": "E",
        "metric": 5,
        "protocol": "DIJKSTRA"
      }
    ]
  }
}
```

---

### 1.3 创建节点

**接口**: `POST /nodes`

**描述**: 创建新的网络节点

**请求Body**:
```json
{
  "nodeId": "G",
  "nodeName": "节点G",
  "ipAddress": "192.168.1.7",
  "macAddress": "AA:BB:CC:DD:EE:07",
  "positionX": 300.0,
  "positionY": 400.0,
  "nodeType": "ROUTER"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "节点创建成功",
  "data": {
    "id": 7,
    "nodeId": "G",
    "nodeName": "节点G",
    "ipAddress": "192.168.1.7",
    "macAddress": "AA:BB:CC:DD:EE:07",
    "positionX": 300.0,
    "positionY": 400.0,
    "nodeType": "ROUTER",
    "status": 1
  }
}
```

---

### 1.4 更新节点

**接口**: `PUT /nodes/{nodeId}`

**描述**: 更新节点信息

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nodeId | String | 是 | 节点ID |

**请求Body**:
```json
{
  "nodeName": "节点A(更新)",
  "positionX": 150.0,
  "positionY": 120.0,
  "status": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "节点更新成功",
  "data": null
}
```

---

### 1.5 删除节点

**接口**: `DELETE /nodes/{nodeId}`

**描述**: 删除指定节点

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nodeId | String | 是 | 节点ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "节点删除成功",
  "data": null
}
```

---

## 2. 网络链路管理

### 2.1 获取所有链路

**接口**: `GET /links`

**描述**: 获取所有网络链路信息

**请求参数**: 无

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "linkId": "AB",
      "sourceNodeId": "A",
      "targetNodeId": "B",
      "weight": 4,
      "bandwidth": 1000,
      "delay": 2,
      "packetLossRate": 0.00,
      "linkType": "ETHERNET",
      "status": 1
    }
  ]
}
```

---

### 2.2 创建链路

**接口**: `POST /links`

**描述**: 创建新的网络链路

**请求Body**:
```json
{
  "sourceNodeId": "A",
  "targetNodeId": "G",
  "weight": 3,
  "bandwidth": 1000,
  "delay": 1,
  "packetLossRate": 0.00,
  "linkType": "ETHERNET"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "链路创建成功",
  "data": {
    "id": 9,
    "linkId": "AG",
    "sourceNodeId": "A",
    "targetNodeId": "G",
    "weight": 3,
    "bandwidth": 1000,
    "delay": 1,
    "packetLossRate": 0.00,
    "linkType": "ETHERNET",
    "status": 1
  }
}
```

---

### 2.3 更新链路

**接口**: `PUT /links/{linkId}`

**描述**: 更新链路信息（如调整权重、带宽等）

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| linkId | String | 是 | 链路ID |

**请求Body**:
```json
{
  "weight": 5,
  "bandwidth": 500,
  "delay": 3,
  "status": 1
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "链路更新成功",
  "data": null
}
```

---

### 2.4 删除链路

**接口**: `DELETE /links/{linkId}`

**描述**: 删除指定链路

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| linkId | String | 是 | 链路ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "链路删除成功",
  "data": null
}
```

---

## 3. 路由算法

### 3.1 计算最短路径 (Dijkstra)

**接口**: `POST /routing/dijkstra`

**描述**: 使用Dijkstra算法计算从源节点到目标节点的最短路径

**请求Body**:
```json
{
  "sourceNodeId": "A",
  "destNodeId": "D"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "algorithm": "DIJKSTRA",
    "sourceNodeId": "A",
    "destNodeId": "D",
    "path": ["A", "E", "D"],
    "pathDetails": [
      {
        "fromNode": "A",
        "toNode": "E",
        "weight": 2,
        "delay": 1
      },
      {
        "fromNode": "E",
        "toNode": "D",
        "weight": 3,
        "delay": 2
      }
    ],
    "totalWeight": 5,
    "totalDelay": 3,
    "hopCount": 2
  }
}
```

---

### 3.2 计算最短路径 (OSPF)

**接口**: `POST /routing/ospf`

**描述**: 使用OSPF算法计算最短路径（基于链路状态）

**请求Body**:
```json
{
  "sourceNodeId": "A",
  "destNodeId": "D",
  "metric": "BANDWIDTH"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "algorithm": "OSPF",
    "sourceNodeId": "A",
    "destNodeId": "D",
    "path": ["A", "B", "C", "D"],
    "cost": 9,
    "hopCount": 3,
    "linkStates": [
      {
        "nodeId": "A",
        "neighbors": ["B", "E"],
        "linkCosts": [4, 2]
      }
    ]
  }
}
```

---

### 3.3 计算最短路径 (RIP)

**接口**: `POST /routing/rip`

**描述**: 使用RIP算法计算最短路径（基于跳数）

**请求Body**:
```json
{
  "sourceNodeId": "A",
  "destNodeId": "D"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "algorithm": "RIP",
    "sourceNodeId": "A",
    "destNodeId": "D",
    "path": ["A", "E", "D"],
    "hopCount": 2,
    "maxHops": 15
  }
}
```

---

### 3.4 获取路由表

**接口**: `GET /routing/table/{nodeId}`

**描述**: 获取指定节点的路由表

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nodeId | String | 是 | 节点ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "nodeId": "A",
    "routes": [
      {
        "destinationNetwork": "192.168.1.4",
        "nextHop": "E",
        "metric": 5,
        "interface": "eth0",
        "protocol": "DIJKSTRA"
      },
      {
        "destinationNetwork": "192.168.1.6",
        "nextHop": "E",
        "metric": 9,
        "interface": "eth0",
        "protocol": "DIJKSTRA"
      }
    ]
  }
}
```

---

## 4. 数据包传输

### 4.1 开始传输

**接口**: `POST /transmission/start`

**描述**: 启动数据包传输过程

**请求Body**:
```json
{
  "sourceNodeId": "A",
  "destNodeId": "D",
  "protocol": "TCP",
  "dataContent": "Hello, Network Simulation!",
  "routingAlgorithm": "DIJKSTRA",
  "sourcePort": 8080,
  "destPort": 80
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "传输启动成功",
  "data": {
    "transmissionId": "uuid-1234-5678-9abc-def0",
    "status": "IN_PROGRESS",
    "estimatedTime": 150
  }
}
```

---

### 4.2 获取传输状态

**接口**: `GET /transmission/{transmissionId}`

**描述**: 查询数据包传输状态

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| transmissionId | String | 是 | 传输ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "transmissionId": "uuid-1234-5678-9abc-def0",
    "sourceNodeId": "A",
    "destNodeId": "D",
    "protocol": "TCP",
    "dataContent": "Hello, Network Simulation!",
    "dataSize": 26,
    "routingAlgorithm": "DIJKSTRA",
    "routingPath": ["A", "E", "D"],
    "hopCount": 2,
    "totalDelay": 150,
    "status": "SUCCESS",
    "startTime": "2024-01-01 12:00:00",
    "endTime": "2024-01-01 12:00:01"
  }
}
```

---

### 4.3 获取传输历史

**接口**: `GET /transmission/history`

**描述**: 获取历史传输记录

**查询参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| sourceNodeId | String | 否 | - | 源节点筛选 |
| destNodeId | String | 否 | - | 目标节点筛选 |
| status | String | 否 | - | 状态筛选 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "page": 1,
    "size": 10,
    "records": [
      {
        "transmissionId": "uuid-1234-5678-9abc-def0",
        "sourceNodeId": "A",
        "destNodeId": "D",
        "protocol": "TCP",
        "dataSize": 26,
        "hopCount": 2,
        "totalDelay": 150,
        "status": "SUCCESS",
        "startTime": "2024-01-01 12:00:00"
      }
    ]
  }
}
```

---

### 4.4 获取跳转详情

**接口**: `GET /transmission/{transmissionId}/hops`

**描述**: 获取数据包在每一跳的详细信息

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| transmissionId | String | 是 | 传输ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "hopSequence": 1,
      "currentNodeId": "A",
      "currentNodeIp": "192.168.1.1",
      "nextNodeId": "E",
      "nextNodeIp": "192.168.1.5",
      "action": "FORWARD",
      "ttl": 64,
      "delay": 1,
      "timestamp": "2024-01-01 12:00:00.100"
    },
    {
      "hopSequence": 2,
      "currentNodeId": "E",
      "currentNodeIp": "192.168.1.5",
      "nextNodeId": "D",
      "nextNodeIp": "192.168.1.4",
      "action": "FORWARD",
      "ttl": 63,
      "delay": 2,
      "timestamp": "2024-01-01 12:00:00.250"
    },
    {
      "hopSequence": 3,
      "currentNodeId": "D",
      "currentNodeIp": "192.168.1.4",
      "nextNodeId": null,
      "action": "DELIVER",
      "ttl": 62,
      "delay": 0,
      "timestamp": "2024-01-01 12:00:00.400"
    }
  ]
}
```

---

## 5. 协议封装

### 5.1 封装数据包

**接口**: `POST /encapsulation/encapsulate`

**描述**: 对数据进行协议栈封装

**请求Body**:
```json
{
  "transmissionId": "uuid-1234-5678-9abc-def0",
  "sourceNodeId": "A",
  "destNodeId": "D",
  "protocol": "TCP",
  "dataContent": "Hello, Network!",
  "sourcePort": 8080,
  "destPort": 80
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "封装成功",
  "data": {
    "transmissionId": "uuid-1234-5678-9abc-def0",
    "layers": [
      {
        "layerNumber": 7,
        "layerName": "Application Layer",
        "headerData": {
          "dataContent": "Hello, Network!",
          "dataLength": 15
        },
        "payloadSize": 15,
        "totalSize": 15
      },
      {
        "layerNumber": 4,
        "layerName": "Transport Layer",
        "headerData": {
          "protocol": "TCP",
          "sourcePort": 8080,
          "destPort": 80,
          "sequenceNumber": 123456,
          "acknowledgment": 0,
          "checksum": "0x1A2B"
        },
        "payloadSize": 15,
        "totalSize": 35
      },
      {
        "layerNumber": 3,
        "layerName": "Network Layer",
        "headerData": {
          "version": 4,
          "sourceIp": "192.168.1.1",
          "destIp": "192.168.1.4",
          "ttl": 64,
          "protocol": 6,
          "checksum": "0x3C4D"
        },
        "payloadSize": 35,
        "totalSize": 55
      },
      {
        "layerNumber": 2,
        "layerName": "Data Link Layer",
        "headerData": {
          "sourceMac": "AA:BB:CC:DD:EE:01",
          "destMac": "AA:BB:CC:DD:EE:04",
          "etherType": "0x0800",
          "fcs": "0x5E6F7A8B"
        },
        "payloadSize": 55,
        "totalSize": 73
      },
      {
        "layerNumber": 1,
        "layerName": "Physical Layer",
        "headerData": {
          "encoding": "Manchester",
          "bitRate": 1000,
          "signalType": "Electrical"
        },
        "payloadSize": 73,
        "totalSize": 73
      }
    ],
    "totalSize": 73
  }
}
```

---

### 5.2 解封装数据包

**接口**: `POST /encapsulation/decapsulate`

**描述**: 对接收到的数据包进行解封装

**请求Body**:
```json
{
  "transmissionId": "uuid-1234-5678-9abc-def0",
  "encapsulatedData": "..."
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "解封装成功",
  "data": {
    "transmissionId": "uuid-1234-5678-9abc-def0",
    "decapsulationSteps": [
      {
        "step": 1,
        "layerName": "Physical Layer",
        "action": "Remove physical encoding",
        "remainingSize": 73
      },
      {
        "step": 2,
        "layerName": "Data Link Layer",
        "action": "Verify FCS and strip Ethernet header",
        "remainingSize": 55
      },
      {
        "step": 3,
        "layerName": "Network Layer",
        "action": "Verify checksum and extract IP information",
        "remainingSize": 35
      },
      {
        "step": 4,
        "layerName": "Transport Layer",
        "action": "Verify TCP checksum and strip TCP header",
        "remainingSize": 15
      },
      {
        "step": 5,
        "layerName": "Application Layer",
        "action": "Deliver data to application",
        "data": "Hello, Network!",
        "remainingSize": 15
      }
    ],
    "originalData": "Hello, Network!"
  }
}
```

---

### 5.3 获取封装详情

**接口**: `GET /encapsulation/{transmissionId}/layers`

**描述**: 获取指定传输的所有协议层封装信息

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| transmissionId | String | 是 | 传输ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "transmissionId": "uuid-1234-5678-9abc-def0",
    "layers": [
      {
        "layerNumber": 7,
        "layerName": "Application Layer",
        "headerData": {...},
        "payloadSize": 15,
        "totalSize": 15
      }
    ]
  }
}
```

---

## 6. 网络拓扑

### 6.1 获取完整拓扑

**接口**: `GET /topology`

**描述**: 获取完整的网络拓扑结构（节点+链路）

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "nodes": [
      {
        "nodeId": "A",
        "nodeName": "节点A",
        "ipAddress": "192.168.1.1",
        "macAddress": "AA:BB:CC:DD:EE:01",
        "positionX": 100.0,
        "positionY": 100.0,
        "nodeType": "ROUTER",
        "status": 1
      }
    ],
    "links": [
      {
        "linkId": "AB",
        "sourceNodeId": "A",
        "targetNodeId": "B",
        "weight": 4,
        "bandwidth": 1000,
        "delay": 2,
        "status": 1
      }
    ],
    "statistics": {
      "totalNodes": 6,
      "totalLinks": 8,
      "activeNodes": 6,
      "activeLinks": 8
    }
  }
}
```

---

### 6.2 导出拓扑

**接口**: `GET /topology/export`

**描述**: 导出网络拓扑配置（JSON格式）

**查询参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| format | String | 否 | json | 导出格式(json/xml) |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "exportTime": "2024-01-01 12:00:00",
    "topology": {...}
  }
}
```

---

### 6.3 导入拓扑

**接口**: `POST /topology/import`

**描述**: 导入网络拓扑配置

**请求Body**:
```json
{
  "nodes": [...],
  "links": [...]
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "拓扑导入成功",
  "data": {
    "importedNodes": 6,
    "importedLinks": 8
  }
}
```

---

## 7. 统计分析

### 7.1 获取传输统计

**接口**: `GET /statistics/transmission`

**描述**: 获取传输统计数据

**查询参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| startDate | String | 否 | - | 开始日期(yyyy-MM-dd) |
| endDate | String | 否 | - | 结束日期(yyyy-MM-dd) |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalTransmissions": 1000,
    "successTransmissions": 950,
    "failedTransmissions": 50,
    "successRate": 95.0,
    "averageDelay": 125,
    "averageHopCount": 2.5,
    "protocolDistribution": {
      "TCP": 700,
      "UDP": 300
    },
    "algorithmDistribution": {
      "DIJKSTRA": 600,
      "OSPF": 300,
      "RIP": 100
    }
  }
}
```

---

### 7.2 获取节点统计

**接口**: `GET /statistics/nodes`

**描述**: 获取节点统计数据

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalNodes": 6,
    "activeNodes": 6,
    "nodeStatistics": [
      {
        "nodeId": "A",
        "sentPackets": 150,
        "receivedPackets": 120,
        "forwardedPackets": 200,
        "droppedPackets": 5,
        "utilization": 65.5
      }
    ]
  }
}
```

---

## 8. 系统配置

### 8.1 获取系统配置

**接口**: `GET /config`

**描述**: 获取系统配置参数

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "defaultRoutingAlgorithm": "DIJKSTRA",
    "maxTtl": 64,
    "packetTimeout": 5000,
    "enableAnimation": true
  }
}
```

---

### 8.2 更新系统配置

**接口**: `PUT /config`

**描述**: 更新系统配置

**请求Body**:
```json
{
  "defaultRoutingAlgorithm": "OSPF",
  "maxTtl": 128,
  "enableAnimation": false
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "配置更新成功",
  "data": null
}
```

---

## WebSocket 接口

### WS 1. 实时传输推送

**接口**: `ws://localhost:8080/ws/transmission/{transmissionId}`

**描述**: WebSocket连接，实时推送传输进度

**接收消息格式**:
```json
{
  "type": "HOP_UPDATE",
  "transmissionId": "uuid-1234-5678-9abc-def0",
  "data": {
    "hopSequence": 2,
    "currentNodeId": "E",
    "nextNodeId": "D",
    "action": "FORWARD",
    "timestamp": "2024-01-01 12:00:00.250"
  }
}
```

**消息类型**:
- `HOP_UPDATE`: 跳转更新
- `TRANSMISSION_COMPLETE`: 传输完成
- `TRANSMISSION_FAILED`: 传输失败
- `ENCAPSULATION_STEP`: 封装步骤

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 1001 | 节点不存在 |
| 1002 | 链路不存在 |
| 1003 | 路径不可达 |
| 2001 | 传输超时 |
| 2002 | 数据包丢失 |
| 3001 | 封装失败 |
| 3002 | 解封装失败 |
| 4001 | 路由计算失败 |
| 5001 | 参数验证失败 |

---

## 附录

### A. 协议类型枚举
- TCP
- UDP
- ICMP
- HTTP

### B. 路由算法枚举
- DIJKSTRA (Dijkstra最短路径算法)
- OSPF (开放式最短路径优先)
- RIP (路由信息协议)
- BGP (边界网关协议)

### C. 节点类型枚举
- ROUTER (路由器)
- SWITCH (交换机)
- HOST (主机)

### D. 链路类型枚举
- ETHERNET (以太网)
- WIRELESS (无线)
- FIBER (光纤)
