package com.network.simulation.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 节点创建请求
 *
 * @author Network Simulation Team
 */
@Data
public class NodeCreateRequest {

    private String nodeId;
    private String nodeName;
    private String ipAddress;
    private String macAddress;
    private BigDecimal positionX;
    private BigDecimal positionY;
    private String nodeType;  // ROUTER/SWITCH/HOST
}
