package com.network.simulation.service;

import com.network.simulation.dto.request.RouteCalculateRequest;
import com.network.simulation.dto.response.RouteResponse;

/**
 * 路由服务接口
 *
 * @author Network Simulation Team
 */
public interface IRoutingService {

    /**
     * 使用Dijkstra算法计算最短路径
     */
    RouteResponse calculateDijkstra(RouteCalculateRequest request);

    /**
     * 使用OSPF算法计算最短路径
     */
    RouteResponse calculateOSPF(RouteCalculateRequest request);

    /**
     * 使用RIP算法计算最短路径
     */
    RouteResponse calculateRIP(RouteCalculateRequest request);

    /**
     * 使用BGP算法计算最短路径
     */
    RouteResponse calculateBGP(RouteCalculateRequest request);
}
