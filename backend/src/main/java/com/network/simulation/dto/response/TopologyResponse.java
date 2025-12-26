package com.network.simulation.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 拓扑响应
 *
 * @author Network Simulation Team
 */
@Data
public class TopologyResponse implements Serializable {

    /**
     * 节点列表
     */
    private List<NodeResponse> nodes;

    /**
     * 链路列表
     */
    private List<LinkResponse> links;

    /**
     * 连通性分析
     */
    @Data
    public static class ConnectivityAnalysis implements Serializable {

        /**
         * 总节点数
         */
        private Integer totalNodes;

        /**
         * 总链路数
         */
        private Integer totalLinks;

        /**
         * 连通分量数量
         */
        private Integer connectedComponents;

        /**
         * 是否全连通
         */
        private Boolean fullyConnected;

        /**
         * 孤立节点列表
         */
        private List<String> isolatedNodes;
    }

    /**
     * 关键节点分析
     */
    @Data
    public static class CriticalNodesAnalysis implements Serializable {

        /**
         * 关键节点列表（按度数排序）
         */
        private List<String> criticalNodes;

        /**
         * 节点度数映射
         */
        private Map<String, Integer> nodeDegrees;

        /**
         * 最大度数
         */
        private Integer maxDegree;

        /**
         * 最小度数
         */
        private Integer minDegree;

        /**
         * 平均度数
         */
        private Double averageDegree;
    }
}
