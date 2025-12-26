package com.network.simulation.algorithm;

import com.network.simulation.entity.NetworkLink;
import com.network.simulation.entity.NetworkNode;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * BGP (Border Gateway Protocol) 路由算法实现
 * 基于路径向量协议，用于自治系统间路由
 *
 * @author Network Simulation Team
 */
@Component
public class BGPAlgorithm {

    /**
     * 计算BGP路由
     * 使用路径向量算法，考虑策略和AS路径
     */
    public RouteResult calculateBGPRoute(String sourceId, String destId,
                                         List<NetworkNode> nodes,
                                         List<NetworkLink> links) {
        // 构建AS拓扑（简化版，假设节点类型决定AS）
        Map<String, Integer> nodeToAS = assignNodesToAS(nodes);
        Map<String, Map<String, PathVector>> pathVectors = new HashMap<>();

        // 初始化路径向量表
        for (NetworkNode node : nodes) {
            pathVectors.put(node.getNodeId(), new HashMap<>());
        }

        // 初始化源节点的路径向量
        PathVector sourcePath = new PathVector();
        sourcePath.asPath.add(nodeToAS.get(sourceId));
        sourcePath.path.add(sourceId);
        sourcePath.localPref = 100; // 默认本地优先级
        pathVectors.get(sourceId).put(destId, sourcePath);

        // BGP路径传播（模拟BGP UPDATE消息交换）
        Map<String, Map<String, LinkInfo>> adjacencyList = buildAdjacencyList(nodes, links);
        boolean updated = true;
        int iteration = 0;

        while (updated && iteration < nodes.size() * 2) {
            updated = false;
            iteration++;

            for (NetworkNode node : nodes) {
                String nodeId = node.getNodeId();
                Map<String, PathVector> nodeVectors = pathVectors.get(nodeId);

                // 获取邻居
                Map<String, LinkInfo> neighbors = adjacencyList.getOrDefault(nodeId, new HashMap<>());

                for (Map.Entry<String, LinkInfo> entry : neighbors.entrySet()) {
                    String neighborId = entry.getKey();
                    Map<String, PathVector> neighborVectors = pathVectors.get(neighborId);

                    // 传播路径信息
                    for (NetworkNode dest : nodes) {
                        String destNodeId = dest.getNodeId();

                        if (destNodeId.equals(nodeId)) {
                            // 到自己的路径
                            if (!nodeVectors.containsKey(destNodeId)) {
                                PathVector selfPath = new PathVector();
                                selfPath.asPath.add(nodeToAS.get(nodeId));
                                selfPath.path.add(nodeId);
                                selfPath.localPref = 100;
                                nodeVectors.put(destNodeId, selfPath);
                            }
                            continue;
                        }

                        PathVector neighborPath = neighborVectors.get(destNodeId);
                        if (neighborPath != null && !neighborPath.asPath.contains(nodeToAS.get(nodeId))) {
                            // 构造新路径（通过邻居）
                            PathVector newPath = new PathVector();
                            newPath.asPath.addAll(neighborPath.asPath);
                            newPath.asPath.add(0, nodeToAS.get(nodeId));
                            newPath.path.addAll(neighborPath.path);
                            newPath.path.add(0, nodeId);
                            newPath.localPref = calculateLocalPreference(entry.getValue());
                            newPath.med = neighborPath.med + entry.getValue().weight;

                            // BGP路径选择
                            PathVector currentPath = nodeVectors.get(destNodeId);
                            if (currentPath == null || isBetterPath(newPath, currentPath)) {
                                nodeVectors.put(destNodeId, newPath);
                                updated = true;
                            }
                        }
                    }
                }
            }
        }

        // 构建路由结果
        return buildRouteResult(sourceId, destId, pathVectors, adjacencyList);
    }

    /**
     * 将节点分配到AS（简化实现）
     */
    private Map<String, Integer> assignNodesToAS(List<NetworkNode> nodes) {
        Map<String, Integer> nodeToAS = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            // 简单分配：每2个节点一个AS
            nodeToAS.put(nodes.get(i).getNodeId(), i / 2 + 1);
        }
        return nodeToAS;
    }

    /**
     * BGP路径选择算法
     * 优先级：Local Preference > AS Path Length > MED > ...
     */
    private boolean isBetterPath(PathVector newPath, PathVector currentPath) {
        // 1. 优先选择Local Preference高的
        if (newPath.localPref != currentPath.localPref) {
            return newPath.localPref > currentPath.localPref;
        }

        // 2. 优先选择AS路径短的
        if (newPath.asPath.size() != currentPath.asPath.size()) {
            return newPath.asPath.size() < currentPath.asPath.size();
        }

        // 3. 优先选择MED小的
        if (newPath.med != currentPath.med) {
            return newPath.med < currentPath.med;
        }

        // 4. 优先选择路径长度短的
        return newPath.path.size() < currentPath.path.size();
    }

    /**
     * 计算本地优先级
     */
    private int calculateLocalPreference(LinkInfo linkInfo) {
        // 基于带宽和延迟计算优先级
        int bandwidthScore = linkInfo.bandwidth / 10;
        int delayPenalty = linkInfo.delay / 10;
        return Math.max(0, 100 + bandwidthScore - delayPenalty);
    }

    /**
     * 构建邻接表
     */
    private Map<String, Map<String, LinkInfo>> buildAdjacencyList(
            List<NetworkNode> nodes, List<NetworkLink> links) {

        Map<String, Map<String, LinkInfo>> adjacencyList = new HashMap<>();

        for (NetworkNode node : nodes) {
            adjacencyList.put(node.getNodeId(), new HashMap<>());
        }

        for (NetworkLink link : links) {
            if (link.getStatus() == 1) {
                LinkInfo linkInfo = new LinkInfo(link.getWeight(), link.getBandwidth(),
                        link.getDelay(), link.getSourceNodeId(), link.getTargetNodeId());

                adjacencyList.get(link.getSourceNodeId()).put(link.getTargetNodeId(), linkInfo);
                adjacencyList.get(link.getTargetNodeId()).put(link.getSourceNodeId(),
                        new LinkInfo(link.getWeight(), link.getBandwidth(), link.getDelay(),
                                link.getTargetNodeId(), link.getSourceNodeId()));
            }
        }

        return adjacencyList;
    }

    /**
     * 构建路由结果
     */
    private RouteResult buildRouteResult(String sourceId, String destId,
                                          Map<String, Map<String, PathVector>> pathVectors,
                                          Map<String, Map<String, LinkInfo>> adjacencyList) {
        RouteResult result = new RouteResult();

        PathVector bestPath = pathVectors.get(sourceId).get(destId);
        if (bestPath == null || bestPath.path.isEmpty()) {
            result.setPath(Collections.emptyList());
            result.setTotalWeight(Integer.MAX_VALUE);
            result.setHopCount(0);
            result.setPathDetails(Collections.emptyList());
            result.setAsPath(Collections.emptyList());
            return result;
        }

        result.setPath(new ArrayList<>(bestPath.path));
        result.setAsPath(new ArrayList<>(bestPath.asPath));
        result.setHopCount(bestPath.path.size() - 1);

        // 构建路径详情
        List<PathDetail> pathDetails = new ArrayList<>();
        int totalWeight = 0;
        for (int i = 0; i < bestPath.path.size() - 1; i++) {
            String from = bestPath.path.get(i);
            String to = bestPath.path.get(i + 1);
            LinkInfo linkInfo = adjacencyList.get(from).get(to);

            PathDetail detail = new PathDetail();
            detail.setFromNode(from);
            detail.setToNode(to);
            detail.setWeight(linkInfo.weight);
            detail.setBandwidth(linkInfo.bandwidth);
            detail.setDelay(linkInfo.delay);
            pathDetails.add(detail);

            totalWeight += linkInfo.weight;
        }
        result.setPathDetails(pathDetails);
        result.setTotalWeight(totalWeight);

        return result;
    }

    @Data
    private static class PathVector {
        List<Integer> asPath = new ArrayList<>();  // AS路径
        List<String> path = new ArrayList<>();     // 节点路径
        int localPref = 100;                       // 本地优先级
        int med = 0;                               // Multi-Exit Discriminator
    }

    @Data
    private static class LinkInfo {
        int weight;
        int bandwidth;
        int delay;
        String sourceNode;
        String targetNode;

        LinkInfo(int weight, int bandwidth, int delay, String sourceNode, String targetNode) {
            this.weight = weight;
            this.bandwidth = bandwidth;
            this.delay = delay;
            this.sourceNode = sourceNode;
            this.targetNode = targetNode;
        }
    }

    @Data
    public static class RouteResult {
        private List<String> path;
        private List<Integer> asPath;
        private Integer totalWeight;
        private Integer hopCount;
        private List<PathDetail> pathDetails;
    }

    @Data
    public static class PathDetail {
        private String fromNode;
        private String toNode;
        private Integer weight;
        private Integer bandwidth;
        private Integer delay;
    }
}
