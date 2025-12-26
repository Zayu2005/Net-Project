package com.network.simulation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.network.simulation.common.exception.BusinessException;
import com.network.simulation.common.result.ResultCode;
import com.network.simulation.dto.request.NodeCreateRequest;
import com.network.simulation.dto.request.NodeUpdateRequest;
import com.network.simulation.dto.response.NodeResponse;
import com.network.simulation.entity.NetworkNode;
import com.network.simulation.mapper.NodeMapper;
import com.network.simulation.service.INodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 节点服务实现
 *
 * @author Network Simulation Team
 */
@Service
@RequiredArgsConstructor
public class NodeServiceImpl implements INodeService {

    private final NodeMapper nodeMapper;

    @Override
    public List<NodeResponse> getAllNodes() {
        List<NetworkNode> nodes = nodeMapper.selectList(null);
        return nodes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NodeResponse getNodeById(String nodeId) {
        NetworkNode node = getNodeByIdOrThrow(nodeId);
        return convertToResponse(node);
    }

    @Override
    public NodeResponse createNode(NodeCreateRequest request) {
        // 检查节点ID是否已存在
        LambdaQueryWrapper<NetworkNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NetworkNode::getNodeId, request.getNodeId());
        if (nodeMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.NODE_ALREADY_EXISTS);
        }

        NetworkNode node = new NetworkNode();
        BeanUtils.copyProperties(request, node);
        node.setStatus(1); // 默认在线

        nodeMapper.insert(node);
        return convertToResponse(node);
    }

    @Override
    public void updateNode(String nodeId, NodeUpdateRequest request) {
        NetworkNode node = getNodeByIdOrThrow(nodeId);

        if (request.getNodeName() != null) {
            node.setNodeName(request.getNodeName());
        }
        if (request.getPositionX() != null) {
            node.setPositionX(request.getPositionX());
        }
        if (request.getPositionY() != null) {
            node.setPositionY(request.getPositionY());
        }
        if (request.getStatus() != null) {
            node.setStatus(request.getStatus());
        }

        nodeMapper.updateById(node);
    }

    @Override
    public void deleteNode(String nodeId) {
        NetworkNode node = getNodeByIdOrThrow(nodeId);
        nodeMapper.deleteById(node.getId());
    }

    /**
     * 根据nodeId获取节点，不存在则抛异常
     */
    private NetworkNode getNodeByIdOrThrow(String nodeId) {
        LambdaQueryWrapper<NetworkNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NetworkNode::getNodeId, nodeId);
        NetworkNode node = nodeMapper.selectOne(wrapper);

        if (node == null) {
            throw new BusinessException(ResultCode.NODE_NOT_FOUND);
        }
        return node;
    }

    /**
     * 转换为响应对象
     */
    private NodeResponse convertToResponse(NetworkNode node) {
        NodeResponse response = new NodeResponse();
        BeanUtils.copyProperties(node, response);
        return response;
    }
}
