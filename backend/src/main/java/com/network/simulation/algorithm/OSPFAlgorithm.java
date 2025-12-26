package com.network.simulation.algorithm;

import com.network.simulation.entity.NetworkLink;
import com.network.simulation.entity.NetworkNode;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * OSPF (Open Shortest Path First) 路由算法实现
 * 基于链路状态路由协议，使用Dijkstra算法
 *
 * @author Network Simulation Team
 */
@Component
public class OSPFAlgorithm {

    /**
     * 计算OSPF路由
     */
    public RouteResult calculateOSPFRoute(String sourceId, String destId,
                                          List<NetworkNode> nodes,
                                          List<NetworkLink> links) {
        // OSPF使用链路状态数据库，每个节点都有完整的网络拓扑
        Map<String, Integer> linkStateCost = new HashMap<>();
        Map<String, Map<String, LinkInfo>> adjacencyList = buildLinkStateDatabase(nodes, links, linkStateCost);

        // 使用Dijkstra算法计算最短路径（基于cost而非weight）
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingInt(nd -> nd.distance));
        Set<String> visited = new HashSet<>();

        // 初始化距离
        for (NetworkNode node : nodes) {
            distances.put(node.getNodeId(), Integer.MAX_VALUE);
        }
        distances.put(sourceId, 0);
        pq.offer(new NodeDistance(sourceId, 0));

        // Dijkstra主循环
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            String currentId = current.nodeId;

            if (visited.contains(currentId)) {
                continue;
            }
            visited.add(currentId);

            if (currentId.equals(destId)) {
                break;
            }

            Map<String, LinkInfo> neighbors = adjacencyList.getOrDefault(currentId, new HashMap<>());
            for (Map.Entry<String, LinkInfo> entry : neighbors.entrySet()) {
                String neighborId = entry.getKey();
                LinkInfo linkInfo = entry.getValue();

                if (!visited.contains(neighborId)) {
                    // OSPF cost计算：考虑带宽和延迟
                    int cost = calculateOSPFCost(linkInfo);
                    int newDistance = distances.get(currentId) + cost;

                    if (newDistance < distances.get(neighborId)) {
                        distances.put(neighborId, newDistance);
                        previous.put(neighborId, currentId);
                        pq.offer(new NodeDistance(neighborId, newDistance));
                    }
                }
            }
        }

        // 重建路径
        return buildRouteResult(sourceId, destId, distances, previous, adjacencyList);
    }

    /**
     * 构建链路状态数据库（Link State Database）
     */
    private Map<String, Map<String, LinkInfo>> buildLinkStateDatabase(
            List<NetworkNode> nodes, List<NetworkLink> links, Map<String, Integer> linkStateCost) {

        Map<String, Map<String, LinkInfo>> lsdb = new HashMap<>();

        // 初始化所有节点
        for (NetworkNode node : nodes) {
            lsdb.put(node.getNodeId(), new HashMap<>());
        }

        // 添加链路信息（双向）
        for (NetworkLink link : links) {
            if (link.getStatus() == 1) { // 仅考虑在线链路
                LinkInfo linkInfo = new LinkInfo(link.getWeight(), link.getBandwidth(),
                        link.getDelay(), link.getSourceNodeId(), link.getTargetNodeId());

                lsdb.get(link.getSourceNodeId()).put(link.getTargetNodeId(), linkInfo);
                lsdb.get(link.getTargetNodeId()).put(link.getSourceNodeId(),
                        new LinkInfo(link.getWeight(), link.getBandwidth(), link.getDelay(),
                                link.getTargetNodeId(), link.getSourceNodeId()));

                // 记录链路成本
                String linkKey = link.getSourceNodeId() + "-" + link.getTargetNodeId();
                linkStateCost.put(linkKey, calculateOSPFCost(linkInfo));
            }
        }

        return lsdb;
    }

    /**
     * 计算OSPF链路成本
     * Cost = 参考带宽 / 链路带宽 + 延迟权重
     */
    private int calculateOSPFCost(LinkInfo linkInfo) {
        int referenceBandwidth = 1000; // 1Gbps参考带宽
        int bandwidthCost = referenceBandwidth / linkInfo.bandwidth;
        int delayCost = linkInfo.delay / 10; // 延迟权重降低
        return bandwidthCost + delayCost;
    }

    /**
     * 构建路由结果
     */
    private RouteResult buildRouteResult(String sourceId, String destId,
                                          Map<String, Integer> distances,
                                          Map<String, String> previous,
                                          Map<String, Map<String, LinkInfo>> adjacencyList) {
        RouteResult result = new RouteResult();

        // 重建路径
        List<String> path = new ArrayList<>();
        String current = destId;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        result.setPath(path);
        result.setTotalWeight(distances.getOrDefault(destId, Integer.MAX_VALUE));
        result.setHopCount(path.size() - 1);

        // 构建路径详情
        List<PathDetail> pathDetails = new ArrayList<>();
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
        }
        result.setPathDetails(pathDetails);

        return result;
    }

    @Data
    private static class NodeDistance {
        String nodeId;
        int distance;

        NodeDistance(String nodeId, int distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }
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
