package com.network.simulation.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 路由响应
 *
 * @author Network Simulation Team
 */
@Data
public class RouteResponse {

    private String algorithm;
    private String sourceNodeId;
    private String destNodeId;
    private List<String> path;
    private Integer totalWeight;
    private Integer totalDelay;
    private Integer hopCount;
    private List<PathDetail> pathDetails;

    @Data
    public static class PathDetail {
        private String fromNode;
        private String toNode;
        private Integer weight;
        private Integer delay;
    }
}
