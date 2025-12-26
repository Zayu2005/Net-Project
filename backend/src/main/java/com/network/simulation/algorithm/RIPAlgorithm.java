package com.network.simulation.algorithm;

import com.network.simulation.entity.NetworkLink;
import com.network.simulation.entity.NetworkNode;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * RIP (Routing Information Protocol) 路由算法实现
 * 基于距离向量协议，以跳数为度量标准
 *
 * @author Network Simulation Team
 */
@Component
public class RIPAlgorithm {

    private static final int MAX_HOP_COUNT = 15; // RIP最大跳数限制
    private static final int INFINITY = 16; // RIP中16表示不可达

    /**
     * 计算RIP路由
     */
    public RouteResult calculateRIPRoute(String sourceId, String destId,
                                         List<NetworkNode> nodes,
                                         List<NetworkLink> links) {
        // 构建邻接表
        Map<String, Map<String, LinkInfo>> adjacencyList = buildAdjacencyList(nodes, links);

        // 初始化距离向量表
        Map<String, DistanceVector> distanceVectors = new HashMap<>();
        for (NetworkNode node : nodes) {
            distanceVectors.put(node.getNodeId(), new DistanceVector());
        }

        // 初始化源节点的距离向量
        DistanceVector sourceVector = distanceVectors.get(sourceId);
        sourceVector.distance.put(sourceId, 0);
        sourceVector.nextHop.put(sourceId, sourceId);

        // Bellman-Ford算法迭代
        boolean updated = true;
        int iteration = 0;
        while (updated && iteration < nodes.size()) {
            updated = false;
            iteration++;

            // 遍历所有节点
            for (NetworkNode node : nodes) {
                String nodeId = node.getNodeId();
                DistanceVector currentVector = distanceVectors.get(nodeId);

                // 获取邻居节点
                Map<String, LinkInfo> neighbors = adjacencyList.getOrDefault(nodeId, new HashMap<>());

                for (Map.Entry<String, LinkInfo> entry : neighbors.entrySet()) {
                    String neighborId = entry.getKey();
                    DistanceVector neighborVector = distanceVectors.get(neighborId);

                    // 尝试通过邻居更新到所有目的地的距离
                    for (NetworkNode dest : nodes) {
                        String destNodeId = dest.getNodeId();

                        int currentDist = currentVector.distance.getOrDefault(destNodeId, INFINITY);
                        int neighborDist = neighborVector.distance.getOrDefault(destNodeId, INFINITY);

                        // RIP使用跳数作为度量，每经过一个路由器跳数+1
                        if (neighborDist < INFINITY) {
                            int newDist = neighborDist + 1;

                            if (newDist < currentDist && newDist <= MAX_HOP_COUNT) {
                                currentVector.distance.put(destNodeId, newDist);
                                currentVector.nextHop.put(destNodeId, neighborId);
                                updated = true;
                            }
                        }
                    }
                }
            }
        }

        // 从源节点的距离向量表重建到目标节点的路径
        return buildRouteResult(sourceId, destId, distanceVectors, adjacencyList);
    }

    /**
     * 构建邻接表
     */
    private Map<String, Map<String, LinkInfo>> buildAdjacencyList(
            List<NetworkNode> nodes, List<NetworkLink> links) {

        Map<String, Map<String, LinkInfo>> adjacencyList = new HashMap<>();

        // 初始化所有节点
        for (NetworkNode node : nodes) {
            adjacencyList.put(node.getNodeId(), new HashMap<>());
        }

        // 添加链路（双向）
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
                                          Map<String, DistanceVector> distanceVectors,
                                          Map<String, Map<String, LinkInfo>> adjacencyList) {
        RouteResult result = new RouteResult();

        DistanceVector sourceVector = distanceVectors.get(sourceId);
        int distance = sourceVector.distance.getOrDefault(destId, INFINITY);

        if (distance >= INFINITY) {
            // 不可达
            result.setPath(Collections.emptyList());
            result.setTotalWeight(Integer.MAX_VALUE);
            result.setHopCount(0);
            result.setPathDetails(Collections.emptyList());
            return result;
        }

        // 重建路径
        List<String> path = new ArrayList<>();
        String current = sourceId;
        path.add(current);

        while (!current.equals(destId)) {
            String nextHop = sourceVector.nextHop.get(destId);
            if (nextHop == null || nextHop.equals(current)) {
                break;
            }
            // 从当前节点向目标移动一步
            current = nextHop;
            path.add(current);

            // 更新到使用当前节点的距离向量
            sourceVector = distanceVectors.get(current);
        }

        result.setPath(path);
        result.setHopCount(path.size() - 1);

        // 构建路径详情
        List<PathDetail> pathDetails = new ArrayList<>();
        int totalWeight = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
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
    private static class DistanceVector {
        Map<String, Integer> distance = new HashMap<>(); // 到各节点的距离（跳数）
        Map<String, String> nextHop = new HashMap<>();   // 到各节点的下一跳
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
