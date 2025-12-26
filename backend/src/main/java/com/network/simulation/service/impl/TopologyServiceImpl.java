package com.network.simulation.service.impl;

import com.network.simulation.dto.response.NodeResponse;
import com.network.simulation.dto.response.LinkResponse;
import com.network.simulation.dto.response.TopologyResponse;
import com.network.simulation.entity.NetworkLink;
import com.network.simulation.entity.NetworkNode;
import com.network.simulation.mapper.LinkMapper;
import com.network.simulation.mapper.NodeMapper;
import com.network.simulation.service.ITopologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 拓扑服务实现
 *
 * @author Network Simulation Team
 */
@Service
@RequiredArgsConstructor
public class TopologyServiceImpl implements ITopologyService {

    private final NodeMapper nodeMapper;
    private final LinkMapper linkMapper;

    @Override
    public TopologyResponse getTopology() {
        List<NetworkNode> nodes = nodeMapper.selectList(null);
        List<NetworkLink> links = linkMapper.selectList(null);

        TopologyResponse response = new TopologyResponse();
        response.setNodes(nodes.stream()
                .map(this::convertToNodeResponse)
                .collect(Collectors.toList()));
        response.setLinks(links.stream()
                .map(this::convertToLinkResponse)
                .collect(Collectors.toList()));

        return response;
    }

    @Override
    public TopologyResponse.ConnectivityAnalysis analyzeConnectivity() {
        List<NetworkNode> nodes = nodeMapper.selectList(null);
        List<NetworkLink> links = linkMapper.selectList(null);

        // 构建邻接表
        Map<String, Set<String>> adjacencyList = buildAdjacencyList(nodes, links);

        // 使用DFS/BFS检测连通分量
        Set<String> visited = new HashSet<>();
        List<Set<String>> connectedComponents = new ArrayList<>();

        for (NetworkNode node : nodes) {
            if (!visited.contains(node.getNodeId())) {
                Set<String> component = new HashSet<>();
                dfs(node.getNodeId(), adjacencyList, visited, component);
                connectedComponents.add(component);
            }
        }

        TopologyResponse.ConnectivityAnalysis analysis = new TopologyResponse.ConnectivityAnalysis();
        analysis.setTotalNodes(nodes.size());
        analysis.setTotalLinks(links.size());
        analysis.setConnectedComponents(connectedComponents.size());
        analysis.setFullyConnected(connectedComponents.size() == 1);
        analysis.setIsolatedNodes(nodes.stream()
                .filter(node -> adjacencyList.getOrDefault(node.getNodeId(), Collections.emptySet()).isEmpty())
                .map(NetworkNode::getNodeId)
                .collect(Collectors.toList()));

        return analysis;
    }

    @Override
    public TopologyResponse.CriticalNodesAnalysis analyzeCriticalNodes() {
        List<NetworkNode> nodes = nodeMapper.selectList(null);
        List<NetworkLink> links = linkMapper.selectList(null);

        Map<String, Set<String>> adjacencyList = buildAdjacencyList(nodes, links);

        // 计算节点度数
        Map<String, Integer> nodeDegrees = new HashMap<>();
        for (NetworkNode node : nodes) {
            nodeDegrees.put(node.getNodeId(),
                    adjacencyList.getOrDefault(node.getNodeId(), Collections.emptySet()).size());
        }

        // 找出关键节点（度数最高的节点）
        List<String> criticalNodes = nodeDegrees.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        TopologyResponse.CriticalNodesAnalysis analysis = new TopologyResponse.CriticalNodesAnalysis();
        analysis.setCriticalNodes(criticalNodes);
        analysis.setNodeDegrees(nodeDegrees);
        analysis.setMaxDegree(nodeDegrees.values().stream().max(Integer::compareTo).orElse(0));
        analysis.setMinDegree(nodeDegrees.values().stream().min(Integer::compareTo).orElse(0));
        analysis.setAverageDegree(nodeDegrees.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0));

        return analysis;
    }

    /**
     * 构建邻接表
     */
    private Map<String, Set<String>> buildAdjacencyList(List<NetworkNode> nodes, List<NetworkLink> links) {
        Map<String, Set<String>> adjacencyList = new HashMap<>();

        // 初始化所有节点
        for (NetworkNode node : nodes) {
            adjacencyList.put(node.getNodeId(), new HashSet<>());
        }

        // 添加链路（双向）
        for (NetworkLink link : links) {
            if (link.getStatus() == 1) { // 仅考虑在线链路
                adjacencyList.get(link.getSourceNodeId()).add(link.getTargetNodeId());
                adjacencyList.get(link.getTargetNodeId()).add(link.getSourceNodeId());
            }
        }

        return adjacencyList;
    }

    /**
     * 深度优先搜索
     */
    private void dfs(String nodeId, Map<String, Set<String>> adjacencyList,
                     Set<String> visited, Set<String> component) {
        visited.add(nodeId);
        component.add(nodeId);

        for (String neighbor : adjacencyList.getOrDefault(nodeId, Collections.emptySet())) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, adjacencyList, visited, component);
            }
        }
    }

    /**
     * 转换为NodeResponse
     */
    private NodeResponse convertToNodeResponse(NetworkNode node) {
        NodeResponse response = new NodeResponse();
        BeanUtils.copyProperties(node, response);
        return response;
    }

    /**
     * 转换为LinkResponse
     */
    private LinkResponse convertToLinkResponse(NetworkLink link) {
        LinkResponse response = new LinkResponse();
        BeanUtils.copyProperties(link, response);
        response.setStatus(convertStatusToString(link.getStatus()));
        return response;
    }

    /**
     * 将状态数字转换为字符串（1 -> UP, 0 -> DOWN）
     */
    private String convertStatusToString(Integer status) {
        return (status != null && status == 1) ? "UP" : "DOWN";
    }
}
