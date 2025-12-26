package com.network.simulation.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 节点更新请求
 *
 * @author Network Simulation Team
 */
@Data
public class NodeUpdateRequest {

    private String nodeName;
    private BigDecimal positionX;
    private BigDecimal positionY;
    private Integer status;
}
