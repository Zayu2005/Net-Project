# SpringBoot 3 后端项目结构

## 项目技术栈

- **框架**: Spring Boot 3.2+
- **Java版本**: JDK 17+
- **构建工具**: Maven 3.9+ / Gradle 8+
- **数据库**: MySQL 8.0+
- **ORM**: MyBatis-Plus 3.5+
- **缓存**: Redis 7+
- **WebSocket**: Spring WebSocket
- **API文档**: Knife4j (Swagger3)
- **工具库**: Hutool, Lombok, MapStruct

---

## 项目目录结构

```
network-simulation-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── network/
│   │   │           └── simulation/
│   │   │               ├── NetworkSimulationApplication.java  # 启动类
│   │   │               ├── config/                            # 配置类
│   │   │               │   ├── MybatisPlusConfig.java
│   │   │               │   ├── RedisConfig.java
│   │   │               │   ├── WebSocketConfig.java
│   │   │               │   ├── CorsConfig.java
│   │   │               │   └── Knife4jConfig.java
│   │   │               ├── controller/                        # 控制器
│   │   │               │   ├── NodeController.java
│   │   │               │   ├── LinkController.java
│   │   │               │   ├── RoutingController.java
│   │   │               │   ├── TransmissionController.java
│   │   │               │   ├── EncapsulationController.java
│   │   │               │   ├── TopologyController.java
│   │   │               │   ├── StatisticsController.java
│   │   │               │   └── ConfigController.java
│   │   │               ├── service/                           # 业务层
│   │   │               │   ├── INodeService.java
│   │   │               │   ├── ILinkService.java
│   │   │               │   ├── IRoutingService.java
│   │   │               │   ├── ITransmissionService.java
│   │   │               │   ├── IEncapsulationService.java
│   │   │               │   ├── ITopologyService.java
│   │   │               │   ├── IStatisticsService.java
│   │   │               │   └── impl/                          # 业务实现
│   │   │               │       ├── NodeServiceImpl.java
│   │   │               │       ├── LinkServiceImpl.java
│   │   │               │       ├── RoutingServiceImpl.java
│   │   │               │       ├── TransmissionServiceImpl.java
│   │   │               │       ├── EncapsulationServiceImpl.java
│   │   │               │       ├── TopologyServiceImpl.java
│   │   │               │       └── StatisticsServiceImpl.java
│   │   │               ├── mapper/                            # 数据访问层
│   │   │               │   ├── NodeMapper.java
│   │   │               │   ├── LinkMapper.java
│   │   │               │   ├── RoutingTableMapper.java
│   │   │               │   ├── PacketTransmissionMapper.java
│   │   │               │   ├── PacketHopMapper.java
│   │   │               │   ├── ProtocolLayerMapper.java
│   │   │               │   └── SystemConfigMapper.java
│   │   │               ├── entity/                            # 实体类
│   │   │               │   ├── NetworkNode.java
│   │   │               │   ├── NetworkLink.java
│   │   │               │   ├── RoutingTable.java
│   │   │               │   ├── PacketTransmission.java
│   │   │               │   ├── PacketHop.java
│   │   │               │   ├── ProtocolLayer.java
│   │   │               │   └── SystemConfig.java
│   │   │               ├── dto/                               # 数据传输对象
│   │   │               │   ├── request/
│   │   │               │   │   ├── NodeCreateRequest.java
│   │   │               │   │   ├── NodeUpdateRequest.java
│   │   │               │   │   ├── LinkCreateRequest.java
│   │   │               │   │   ├── RouteCalculateRequest.java
│   │   │               │   │   ├── TransmissionStartRequest.java
│   │   │               │   │   └── EncapsulationRequest.java
│   │   │               │   └── response/
│   │   │               │       ├── NodeResponse.java
│   │   │               │       ├── LinkResponse.java
│   │   │               │       ├── RouteResponse.java
│   │   │               │       ├── TransmissionResponse.java
│   │   │               │       ├── HopResponse.java
│   │   │               │       └── EncapsulationResponse.java
│   │   │               ├── vo/                                # 视图对象
│   │   │               │   ├── TopologyVO.java
│   │   │               │   ├── StatisticsVO.java
│   │   │               │   └── RoutePathVO.java
│   │   │               ├── algorithm/                         # 路由算法
│   │   │               │   ├── DijkstraAlgorithm.java
│   │   │               │   ├── OSPFAlgorithm.java
│   │   │               │   ├── RIPAlgorithm.java
│   │   │               │   ├── BGPAlgorithm.java
│   │   │               │   └── RouteAlgorithmFactory.java
│   │   │               ├── protocol/                          # 协议处理
│   │   │               │   ├── ApplicationLayer.java
│   │   │               │   ├── TransportLayer.java
│   │   │               │   ├── NetworkLayer.java
│   │   │               │   ├── DataLinkLayer.java
│   │   │               │   ├── PhysicalLayer.java
│   │   │               │   └── ProtocolStackProcessor.java
│   │   │               ├── websocket/                         # WebSocket
│   │   │               │   ├── TransmissionWebSocketHandler.java
│   │   │               │   └── WebSocketMessageService.java
│   │   │               ├── common/                            # 公共类
│   │   │               │   ├── result/
│   │   │               │   │   ├── Result.java                # 统一返回结果
│   │   │               │   │   └── ResultCode.java            # 状态码枚举
│   │   │               │   ├── exception/
│   │   │               │   │   ├── BusinessException.java
│   │   │               │   │   └── GlobalExceptionHandler.java
│   │   │               │   ├── constant/
│   │   │               │   │   ├── ProtocolConstant.java
│   │   │               │   │   ├── AlgorithmConstant.java
│   │   │               │   │   └── SystemConstant.java
│   │   │               │   └── enums/
│   │   │               │       ├── NodeTypeEnum.java
│   │   │               │       ├── LinkTypeEnum.java
│   │   │               │       ├── ProtocolEnum.java
│   │   │               │       ├── AlgorithmEnum.java
│   │   │               │       └── TransmissionStatusEnum.java
│   │   │               └── util/                              # 工具类
│   │   │                   ├── NetworkUtil.java
│   │   │                   ├── ChecksumUtil.java
│   │   │                   ├── MACUtil.java
│   │   │                   └── IPUtil.java
│   │   └── resources/
│   │       ├── mapper/                                        # MyBatis映射文件
│   │       │   ├── NodeMapper.xml
│   │       │   ├── LinkMapper.xml
│   │       │   ├── RoutingTableMapper.xml
│   │       │   ├── PacketTransmissionMapper.xml
│   │       │   ├── PacketHopMapper.xml
│   │       │   ├── ProtocolLayerMapper.xml
│   │       │   └── SystemConfigMapper.xml
│   │       ├── db/                                            # 数据库脚本
│   │       │   ├── schema.sql                                 # 建表脚本
│   │       │   └── data.sql                                   # 初始数据
│   │       ├── application.yml                                # 主配置文件
│   │       ├── application-dev.yml                            # 开发环境配置
│   │       ├── application-prod.yml                           # 生产环境配置
│   │       └── logback-spring.xml                             # 日志配置
│   └── test/
│       └── java/
│           └── com/
│               └── network/
│                   └── simulation/
│                       ├── algorithm/                         # 算法测试
│                       │   ├── DijkstraAlgorithmTest.java
│                       │   └── OSPFAlgorithmTest.java
│                       ├── service/                           # 服务测试
│                       │   ├── RoutingServiceTest.java
│                       │   └── TransmissionServiceTest.java
│                       └── NetworkSimulationApplicationTests.java
├── .gitignore
├── pom.xml                                                    # Maven配置
└── README.md
```

---

## 核心配置文件

### 1. pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>

    <groupId>com.network</groupId>
    <artifactId>simulation-backend</artifactId>
    <version>1.0.0</version>
    <name>network-simulation-backend</name>
    <description>网络数据传输与封装模拟系统后端</description>

    <properties>
        <java.version>17</java.version>
        <mybatis-plus.version>3.5.5</mybatis-plus.version>
        <knife4j.version>4.4.0</knife4j.version>
        <hutool.version>5.8.24</hutool.version>
        <mapstruct.version>1.5.5.Final</mapstruct.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- MyBatis Plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>

        <!-- MySQL Driver -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Knife4j (Swagger3) -->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
            <version>${knife4j.version}</version>
        </dependency>

        <!-- Hutool -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>${hutool.version}</version>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- MapStruct -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>

        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <version>${mapstruct.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- Jackson for JSON -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### 2. application.yml
```yaml
server:
  port: 8080
  servlet:
    context-path: /

spring:
  application:
    name: network-simulation-backend

  profiles:
    active: dev

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/network_simulation?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: root

  data:
    redis:
      host: localhost
      port: 6379
      password:
      database: 0
      timeout: 5000ms
      lettuce:
        pool:
          max-active: 8
          max-wait: -1ms
          max-idle: 8
          min-idle: 0

  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8

# MyBatis Plus
mybatis-plus:
  mapper-locations: classpath*:mapper/**/*.xml
  type-aliases-package: com.network.simulation.entity
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

# Knife4j
knife4j:
  enable: true
  setting:
    language: zh_cn
  openapi:
    title: 网络模拟系统API文档
    description: 网络数据传输与封装模拟系统接口文档
    version: v1.0.0
    contact:
      name: Network Simulation Team
      email: support@example.com
    group:
      default:
        group-name: 默认分组
        api-rule: package
        api-rule-resources:
          - com.network.simulation.controller

# Logging
logging:
  level:
    root: INFO
    com.network.simulation: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n"
  file:
    name: logs/network-simulation.log
    max-size: 10MB
    max-history: 30
```

### 3. application-dev.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/network_simulation?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: root

  data:
    redis:
      host: localhost
      port: 6379
      password:

logging:
  level:
    root: INFO
    com.network.simulation: DEBUG
```

### 4. application-prod.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://your-production-db:3306/network_simulation?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=true
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}
      password: ${REDIS_PASSWORD}

logging:
  level:
    root: WARN
    com.network.simulation: INFO
```

---

## 核心代码示例

### 1. 启动类
```java
package com.network.simulation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.network.simulation.mapper")
public class NetworkSimulationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NetworkSimulationApplication.class, args);
    }
}
```

### 2. 统一返回结果
```java
package com.network.simulation.common.result;

import lombok.Data;
import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.ERROR.getCode());
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
```

### 3. 状态码枚举
```java
package com.network.simulation.common.result;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "success"),
    ERROR(500, "error"),
    BAD_REQUEST(400, "请求参数错误"),
    NOT_FOUND(404, "资源不存在"),

    // 业务错误码
    NODE_NOT_FOUND(1001, "节点不存在"),
    LINK_NOT_FOUND(1002, "链路不存在"),
    PATH_NOT_REACHABLE(1003, "路径不可达"),
    TRANSMISSION_TIMEOUT(2001, "传输超时"),
    PACKET_LOST(2002, "数据包丢失"),
    ENCAPSULATION_FAILED(3001, "封装失败"),
    DECAPSULATION_FAILED(3002, "解封装失败"),
    ROUTING_FAILED(4001, "路由计算失败");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
```

### 4. 全局异常处理
```java
package com.network.simulation.common.exception;

import com.network.simulation.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error("系统异常，请稍后重试");
    }
}
```

### 5. 实体类示例
```java
package com.network.simulation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("network_node")
public class NetworkNode {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String nodeId;
    private String nodeName;
    private String ipAddress;
    private String macAddress;
    private BigDecimal positionX;
    private BigDecimal positionY;
    private String nodeType;
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
```

### 6. Controller示例
```java
package com.network.simulation.controller;

import com.network.simulation.common.result.Result;
import com.network.simulation.dto.request.NodeCreateRequest;
import com.network.simulation.dto.request.NodeUpdateRequest;
import com.network.simulation.dto.response.NodeResponse;
import com.network.simulation.service.INodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "网络节点管理")
@RestController
@RequestMapping("/api/v1/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final INodeService nodeService;

    @Operation(summary = "获取所有节点")
    @GetMapping
    public Result<List<NodeResponse>> getAllNodes() {
        return Result.success(nodeService.getAllNodes());
    }

    @Operation(summary = "获取节点详情")
    @GetMapping("/{nodeId}")
    public Result<NodeResponse> getNode(@PathVariable String nodeId) {
        return Result.success(nodeService.getNodeById(nodeId));
    }

    @Operation(summary = "创建节点")
    @PostMapping
    public Result<NodeResponse> createNode(@RequestBody NodeCreateRequest request) {
        return Result.success(nodeService.createNode(request));
    }

    @Operation(summary = "更新节点")
    @PutMapping("/{nodeId}")
    public Result<Void> updateNode(@PathVariable String nodeId,
                                   @RequestBody NodeUpdateRequest request) {
        nodeService.updateNode(nodeId, request);
        return Result.success();
    }

    @Operation(summary = "删除节点")
    @DeleteMapping("/{nodeId}")
    public Result<Void> deleteNode(@PathVariable String nodeId) {
        nodeService.deleteNode(nodeId);
        return Result.success();
    }
}
```

### 7. Service接口示例
```java
package com.network.simulation.service;

import com.network.simulation.dto.request.NodeCreateRequest;
import com.network.simulation.dto.request.NodeUpdateRequest;
import com.network.simulation.dto.response.NodeResponse;

import java.util.List;

public interface INodeService {
    List<NodeResponse> getAllNodes();
    NodeResponse getNodeById(String nodeId);
    NodeResponse createNode(NodeCreateRequest request);
    void updateNode(String nodeId, NodeUpdateRequest request);
    void deleteNode(String nodeId);
}
```

### 8. Dijkstra算法实现
```java
package com.network.simulation.algorithm;

import com.network.simulation.entity.NetworkLink;
import com.network.simulation.entity.NetworkNode;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DijkstraAlgorithm {

    @Data
    public static class RouteResult {
        private List<String> path;
        private Integer totalWeight;
        private Integer hopCount;
        private List<PathDetail> pathDetails;
    }

    @Data
    public static class PathDetail {
        private String fromNode;
        private String toNode;
        private Integer weight;
        private Integer delay;
    }

    public RouteResult calculateShortestPath(String sourceId, String destId,
                                            List<NetworkNode> nodes,
                                            List<NetworkLink> links) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> unvisited = new HashSet<>();

        // 初始化
        for (NetworkNode node : nodes) {
            distances.put(node.getNodeId(), Integer.MAX_VALUE);
            previous.put(node.getNodeId(), null);
            unvisited.add(node.getNodeId());
        }
        distances.put(sourceId, 0);

        // 构建邻接表
        Map<String, List<NetworkLink>> adjacencyList = buildAdjacencyList(links);

        while (!unvisited.isEmpty()) {
            // 找到未访问节点中距离最小的
            String current = findMinDistance(unvisited, distances);
            if (current == null || current.equals(destId)) {
                break;
            }

            unvisited.remove(current);

            // 更新邻居节点距离
            List<NetworkLink> neighbors = adjacencyList.getOrDefault(current, new ArrayList<>());
            for (NetworkLink link : neighbors) {
                String neighbor = link.getSourceNodeId().equals(current)
                    ? link.getTargetNodeId() : link.getSourceNodeId();

                if (unvisited.contains(neighbor)) {
                    int alt = distances.get(current) + link.getWeight();
                    if (alt < distances.get(neighbor)) {
                        distances.put(neighbor, alt);
                        previous.put(neighbor, current);
                    }
                }
            }
        }

        // 重建路径
        return buildRouteResult(sourceId, destId, previous, distances, links);
    }

    private Map<String, List<NetworkLink>> buildAdjacencyList(List<NetworkLink> links) {
        Map<String, List<NetworkLink>> adjacencyList = new HashMap<>();
        for (NetworkLink link : links) {
            adjacencyList.computeIfAbsent(link.getSourceNodeId(), k -> new ArrayList<>()).add(link);
            adjacencyList.computeIfAbsent(link.getTargetNodeId(), k -> new ArrayList<>()).add(link);
        }
        return adjacencyList;
    }

    private String findMinDistance(Set<String> unvisited, Map<String, Integer> distances) {
        String minNode = null;
        int minDistance = Integer.MAX_VALUE;

        for (String node : unvisited) {
            int distance = distances.get(node);
            if (distance < minDistance) {
                minDistance = distance;
                minNode = node;
            }
        }

        return minNode;
    }

    private RouteResult buildRouteResult(String sourceId, String destId,
                                         Map<String, String> previous,
                                         Map<String, Integer> distances,
                                         List<NetworkLink> links) {
        RouteResult result = new RouteResult();
        List<String> path = new ArrayList<>();

        String current = destId;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        result.setPath(path);
        result.setTotalWeight(distances.get(destId));
        result.setHopCount(path.size() - 1);

        // 构建路径详情
        List<PathDetail> pathDetails = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            NetworkLink link = findLink(from, to, links);

            if (link != null) {
                PathDetail detail = new PathDetail();
                detail.setFromNode(from);
                detail.setToNode(to);
                detail.setWeight(link.getWeight());
                detail.setDelay(link.getDelay());
                pathDetails.add(detail);
            }
        }
        result.setPathDetails(pathDetails);

        return result;
    }

    private NetworkLink findLink(String from, String to, List<NetworkLink> links) {
        return links.stream()
            .filter(link ->
                (link.getSourceNodeId().equals(from) && link.getTargetNodeId().equals(to)) ||
                (link.getSourceNodeId().equals(to) && link.getTargetNodeId().equals(from)))
            .findFirst()
            .orElse(null);
    }
}
```

### 9. WebSocket配置
```java
package com.network.simulation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new TransmissionWebSocketHandler(), "/ws/transmission/**")
                .setAllowedOrigins("*");
    }
}
```

---

## 编译与运行

### Maven编译
```bash
mvn clean package -DskipTests
```

### 运行
```bash
# 开发环境
java -jar target/simulation-backend-1.0.0.jar --spring.profiles.active=dev

# 生产环境
java -jar target/simulation-backend-1.0.0.jar --spring.profiles.active=prod
```

---

## API文档访问

启动后访问: http://localhost:8080/doc.html

---

## Docker部署

### Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/simulation-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml
```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: network-simulation-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: network_simulation
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./db:/docker-entrypoint-initdb.d

  redis:
    image: redis:7
    container_name: network-simulation-redis
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data

  backend:
    build: .
    container_name: network-simulation-backend
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_URL: jdbc:mysql://mysql:3306/network_simulation
      REDIS_HOST: redis
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis

volumes:
  mysql-data:
  redis-data:
```
