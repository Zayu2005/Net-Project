package com.network.simulation.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 节点响应
 *
 * @author Network Simulation Team
 */
@Data
public class NodeResponse {

    private Long id;
    private String nodeId;
    private String nodeName;
    private String ipAddress;
    private String macAddress;
    private BigDecimal positionX;
    private BigDecimal positionY;
    private String nodeType;
    private Integer status;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
