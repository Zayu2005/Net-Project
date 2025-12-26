package com.network.simulation.dto.request;

import lombok.Data;

/**
 * 路由计算请求
 *
 * @author Network Simulation Team
 */
@Data
public class RouteCalculateRequest {

    private String sourceNodeId;
    private String destNodeId;
    private String metric;  // WEIGHT/BANDWIDTH/DELAY (用于OSPF)
}
