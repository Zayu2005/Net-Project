package com.network.simulation.algorithm;

import com.network.simulation.entity.NetworkLink;
import com.network.simulation.entity.NetworkNode;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Dijkstra最短路径算法实现
 *
 * @author Network Simulation Team
 */
@Component
public class DijkstraAlgorithm {

    /**
     * 路由计算结果
     */
    @Data
    public static class RouteResult {
        /**
         * 路径节点列表
         */
        private List<String> path;

        /**
         * 总权重
         */
        private Integer totalWeight;

        /**
         * 跳数
         */
        private Integer hopCount;

        /**
         * 路径详情
         */
        private List<PathDetail> pathDetails;
    }

    /**
     * 路径详情
     */
    @Data
    public static class PathDetail {
        private String fromNode;
        private String toNode;
        private Integer weight;
        private Integer delay;
    }

    /**
     * 计算最短路径
     *
     * @param sourceId 源节点ID
     * @param destId   目标节点ID
     * @param nodes    所有节点
     * @param links    所有链路
     * @return 路由结果
     */
    public RouteResult calculateShortestPath(String sourceId, String destId,
                                            List<NetworkNode> nodes,
                                            List<NetworkLink> links) {
        // 初始化距离表和前驱节点表
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> unvisited = new HashSet<>();

        // 初始化所有节点的距离为无穷大
        for (NetworkNode node : nodes) {
            distances.put(node.getNodeId(), Integer.MAX_VALUE);
            previous.put(node.getNodeId(), null);
            unvisited.add(node.getNodeId());
        }
        distances.put(sourceId, 0);

        // 构建邻接表
        Map<String, List<NetworkLink>> adjacencyList = buildAdjacencyList(links);

        // Dijkstra算法主循环
        while (!unvisited.isEmpty()) {
            // 找到未访问节点中距离最小的节点
            String current = findMinDistance(unvisited, distances);
            if (current == null || current.equals(destId)) {
                break;
            }

            unvisited.remove(current);

            // 更新邻居节点的距离
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

    /**
     * 构建邻接表
     */
    private Map<String, List<NetworkLink>> buildAdjacencyList(List<NetworkLink> links) {
        Map<String, List<NetworkLink>> adjacencyList = new HashMap<>();
        for (NetworkLink link : links) {
            if (link.getStatus() == 1) {  // 只考虑激活的链路
                adjacencyList.computeIfAbsent(link.getSourceNodeId(), k -> new ArrayList<>()).add(link);
                adjacencyList.computeIfAbsent(link.getTargetNodeId(), k -> new ArrayList<>()).add(link);
            }
        }
        return adjacencyList;
    }

    /**
     * 找到未访问节点中距离最小的节点
     */
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

    /**
     * 构建路由结果
     */
    private RouteResult buildRouteResult(String sourceId, String destId,
                                         Map<String, String> previous,
                                         Map<String, Integer> distances,
                                         List<NetworkLink> links) {
        RouteResult result = new RouteResult();
        List<String> path = new ArrayList<>();

        // 从目标节点回溯到源节点
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

    /**
     * 查找两个节点之间的链路
     */
    private NetworkLink findLink(String from, String to, List<NetworkLink> links) {
        return links.stream()
                .filter(link ->
                        (link.getSourceNodeId().equals(from) && link.getTargetNodeId().equals(to)) ||
                        (link.getSourceNodeId().equals(to) && link.getTargetNodeId().equals(from)))
                .findFirst()
                .orElse(null);
    }
}
