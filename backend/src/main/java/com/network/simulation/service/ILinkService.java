package com.network.simulation.service;

import com.network.simulation.dto.request.LinkCreateRequest;
import com.network.simulation.dto.request.LinkUpdateRequest;
import com.network.simulation.dto.response.LinkResponse;

import java.util.List;

/**
 * 链路服务接口
 *
 * @author Network Simulation Team
 */
public interface ILinkService {

    /**
     * 获取所有链路
     */
    List<LinkResponse> getAllLinks();

    /**
     * 根据链路ID获取链路详情
     */
    LinkResponse getLinkById(String linkId);

    /**
     * 创建链路
     */
    LinkResponse createLink(LinkCreateRequest request);

    /**
     * 更新链路
     */
    void updateLink(String linkId, LinkUpdateRequest request);

    /**
     * 删除链路
     */
    void deleteLink(String linkId);

    /**
     * 获取节点相关的所有链路
     */
    List<LinkResponse> getLinksByNodeId(String nodeId);
}
