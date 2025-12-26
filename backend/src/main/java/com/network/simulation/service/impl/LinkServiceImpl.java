package com.network.simulation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.network.simulation.common.exception.BusinessException;
import com.network.simulation.common.result.ResultCode;
import com.network.simulation.dto.request.LinkCreateRequest;
import com.network.simulation.dto.request.LinkUpdateRequest;
import com.network.simulation.dto.response.LinkResponse;
import com.network.simulation.entity.NetworkLink;
import com.network.simulation.entity.NetworkNode;
import com.network.simulation.mapper.LinkMapper;
import com.network.simulation.mapper.NodeMapper;
import com.network.simulation.service.ILinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 链路服务实现
 *
 * @author Network Simulation Team
 */
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements ILinkService {

    private final LinkMapper linkMapper;
    private final NodeMapper nodeMapper;

    @Override
    public List<LinkResponse> getAllLinks() {
        List<NetworkLink> links = linkMapper.selectList(null);
        return links.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LinkResponse getLinkById(String linkId) {
        NetworkLink link = getLinkByIdOrThrow(linkId);
        return convertToResponse(link);
    }

    @Override
    public LinkResponse createLink(LinkCreateRequest request) {
        // 验证源节点和目标节点是否存在
        validateNodeExists(request.getSourceNodeId());
        validateNodeExists(request.getTargetNodeId());

        // 检查是否已存在相同的链路
        LambdaQueryWrapper<NetworkLink> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NetworkLink::getSourceNodeId, request.getSourceNodeId())
                .eq(NetworkLink::getTargetNodeId, request.getTargetNodeId());
        if (linkMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.LINK_ALREADY_EXISTS);
        }

        NetworkLink link = new NetworkLink();
        BeanUtils.copyProperties(request, link);
        link.setLinkId(generateLinkId(request.getSourceNodeId(), request.getTargetNodeId()));
        link.setStatus(convertStatusToInt(request.getStatus())); // 转换状态为数字

        linkMapper.insert(link);
        return convertToResponse(link);
    }

    @Override
    public void updateLink(String linkId, LinkUpdateRequest request) {
        NetworkLink link = getLinkByIdOrThrow(linkId);

        if (request.getWeight() != null) {
            link.setWeight(request.getWeight());
        }
        if (request.getBandwidth() != null) {
            link.setBandwidth(request.getBandwidth());
        }
        if (request.getDelay() != null) {
            link.setDelay(request.getDelay());
        }
        if (request.getPacketLossRate() != null) {
            link.setPacketLossRate(request.getPacketLossRate());
        }
        if (request.getStatus() != null) {
            link.setStatus(convertStatusToInt(request.getStatus()));
        }

        linkMapper.updateById(link);
    }

    @Override
    public void deleteLink(String linkId) {
        NetworkLink link = getLinkByIdOrThrow(linkId);
        linkMapper.deleteById(link.getId());
    }

    @Override
    public List<LinkResponse> getLinksByNodeId(String nodeId) {
        // 验证节点存在
        validateNodeExists(nodeId);

        LambdaQueryWrapper<NetworkLink> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NetworkLink::getSourceNodeId, nodeId)
                .or()
                .eq(NetworkLink::getTargetNodeId, nodeId);

        List<NetworkLink> links = linkMapper.selectList(wrapper);
        return links.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据linkId获取链路，不存在则抛异常
     */
    private NetworkLink getLinkByIdOrThrow(String linkId) {
        LambdaQueryWrapper<NetworkLink> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NetworkLink::getLinkId, linkId);
        NetworkLink link = linkMapper.selectOne(wrapper);
        if (link == null) {
            throw new BusinessException(ResultCode.LINK_NOT_FOUND);
        }
        return link;
    }

    /**
     * 生成链路ID
     */
    private String generateLinkId(String sourceNodeId, String targetNodeId) {
        return "LINK_" + sourceNodeId + "_" + targetNodeId;
    }

    /**
     * 验证节点是否存在
     */
    private void validateNodeExists(String nodeId) {
        LambdaQueryWrapper<NetworkNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NetworkNode::getNodeId, nodeId);
        if (nodeMapper.selectCount(wrapper) == 0) {
            throw new BusinessException(ResultCode.NODE_NOT_FOUND);
        }
    }

    /**
     * 转换为响应对象
     */
    private LinkResponse convertToResponse(NetworkLink link) {
        LinkResponse response = new LinkResponse();
        BeanUtils.copyProperties(link, response);
        response.setStatus(convertStatusToString(link.getStatus()));
        return response;
    }

    /**
     * 将状态字符串转换为数字（UP -> 1, DOWN -> 0）
     */
    private Integer convertStatusToInt(String status) {
        if (status == null) {
            return 1; // 默认在线
        }
        return "UP".equalsIgnoreCase(status) ? 1 : 0;
    }

    /**
     * 将状态数字转换为字符串（1 -> UP, 0 -> DOWN）
     */
    private String convertStatusToString(Integer status) {
        return (status != null && status == 1) ? "UP" : "DOWN";
    }
}
