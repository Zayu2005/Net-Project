package com.network.simulation.service;

import com.network.simulation.dto.response.TopologyResponse;

/**
 * 拓扑服务接口
 *
 * @author Network Simulation Team
 */
public interface ITopologyService {

    /**
     * 获取完整的网络拓扑
     */
    TopologyResponse getTopology();

    /**
     * 分析网络拓扑连通性
     */
    TopologyResponse.ConnectivityAnalysis analyzeConnectivity();

    /**
     * 计算网络拓扑关键节点
     */
    TopologyResponse.CriticalNodesAnalysis analyzeCriticalNodes();
}
