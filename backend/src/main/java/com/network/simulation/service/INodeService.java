package com.network.simulation.service;

import com.network.simulation.dto.request.NodeCreateRequest;
import com.network.simulation.dto.request.NodeUpdateRequest;
import com.network.simulation.dto.response.NodeResponse;

import java.util.List;

/**
 * 节点服务接口
 *
 * @author Network Simulation Team
 */
public interface INodeService {

    /**
     * 获取所有节点
     */
    List<NodeResponse> getAllNodes();

    /**
     * 根据ID获取节点
     */
    NodeResponse getNodeById(String nodeId);

    /**
     * 创建节点
     */
    NodeResponse createNode(NodeCreateRequest request);

    /**
     * 更新节点
     */
    void updateNode(String nodeId, NodeUpdateRequest request);

    /**
     * 删除节点
     */
    void deleteNode(String nodeId);
}
