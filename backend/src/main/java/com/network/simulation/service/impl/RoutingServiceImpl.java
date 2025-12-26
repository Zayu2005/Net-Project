package com.network.simulation.service.impl;

import com.network.simulation.algorithm.BGPAlgorithm;
import com.network.simulation.algorithm.DijkstraAlgorithm;
import com.network.simulation.algorithm.OSPFAlgorithm;
import com.network.simulation.algorithm.RIPAlgorithm;
import com.network.simulation.common.exception.BusinessException;
import com.network.simulation.common.result.ResultCode;
import com.network.simulation.dto.request.RouteCalculateRequest;
import com.network.simulation.dto.response.RouteResponse;
import com.network.simulation.entity.NetworkLink;
import com.network.simulation.entity.NetworkNode;
import com.network.simulation.mapper.LinkMapper;
import com.network.simulation.mapper.NodeMapper;
import com.network.simulation.service.IRoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 路由服务实现
 *
 * @author Network Simulation Team
 */
@Service
@RequiredArgsConstructor
public class RoutingServiceImpl implements IRoutingService {

    private final DijkstraAlgorithm dijkstraAlgorithm;
    private final OSPFAlgorithm ospfAlgorithm;
    private final RIPAlgorithm ripAlgorithm;
    private final BGPAlgorithm bgpAlgorithm;
    private final NodeMapper nodeMapper;
    private final LinkMapper linkMapper;

    @Override
    public RouteResponse calculateDijkstra(RouteCalculateRequest request) {
        // 获取所有节点和链路
        List<NetworkNode> nodes = nodeMapper.selectList(null);
        List<NetworkLink> links = linkMapper.selectList(null);

        // 调用Dijkstra算法
        DijkstraAlgorithm.RouteResult result = dijkstraAlgorithm.calculateShortestPath(
                request.getSourceNodeId(),
                request.getDestNodeId(),
                nodes,
                links
        );

        // 检查路径是否可达
        if (result.getPath() == null || result.getPath().isEmpty() ||
            result.getTotalWeight() == Integer.MAX_VALUE) {
            throw new BusinessException(ResultCode.PATH_NOT_REACHABLE);
        }

        // 转换为响应对象
        RouteResponse response = new RouteResponse();
        response.setAlgorithm("DIJKSTRA");
        response.setSourceNodeId(request.getSourceNodeId());
        response.setDestNodeId(request.getDestNodeId());
        response.setPath(result.getPath());
        response.setTotalWeight(result.getTotalWeight());
        response.setHopCount(result.getHopCount());

        // 计算总延迟
        int totalDelay = result.getPathDetails().stream()
                .mapToInt(DijkstraAlgorithm.PathDetail::getDelay)
                .sum();
        response.setTotalDelay(totalDelay);

        // 转换路径详情
        List<RouteResponse.PathDetail> pathDetails = result.getPathDetails().stream()
                .map(detail -> {
                    RouteResponse.PathDetail pd = new RouteResponse.PathDetail();
                    BeanUtils.copyProperties(detail, pd);
                    return pd;
                })
                .collect(Collectors.toList());
        response.setPathDetails(pathDetails);

        return response;
    }

    @Override
    public RouteResponse calculateOSPF(RouteCalculateRequest request) {
        // 获取所有节点和链路
        List<NetworkNode> nodes = nodeMapper.selectList(null);
        List<NetworkLink> links = linkMapper.selectList(null);

        // 调用OSPF算法
        OSPFAlgorithm.RouteResult result = ospfAlgorithm.calculateOSPFRoute(
                request.getSourceNodeId(),
                request.getDestNodeId(),
                nodes,
                links
        );

        // 检查路径是否可达
        if (result.getPath() == null || result.getPath().isEmpty() ||
            result.getTotalWeight() == Integer.MAX_VALUE) {
            throw new BusinessException(ResultCode.PATH_NOT_REACHABLE);
        }

        // 转换为响应对象
        return buildRouteResponse("OSPF", request, result.getPath(), result.getTotalWeight(),
                result.getHopCount(), result.getPathDetails());
    }

    @Override
    public RouteResponse calculateRIP(RouteCalculateRequest request) {
        // 获取所有节点和链路
        List<NetworkNode> nodes = nodeMapper.selectList(null);
        List<NetworkLink> links = linkMapper.selectList(null);

        // 调用RIP算法
        RIPAlgorithm.RouteResult result = ripAlgorithm.calculateRIPRoute(
                request.getSourceNodeId(),
                request.getDestNodeId(),
                nodes,
                links
        );

        // 检查路径是否可达
        if (result.getPath() == null || result.getPath().isEmpty() ||
            result.getTotalWeight() == Integer.MAX_VALUE) {
            throw new BusinessException(ResultCode.PATH_NOT_REACHABLE);
        }

        // 转换为响应对象
        return buildRouteResponse("RIP", request, result.getPath(), result.getTotalWeight(),
                result.getHopCount(), result.getPathDetails());
    }

    @Override
    public RouteResponse calculateBGP(RouteCalculateRequest request) {
        // 获取所有节点和链路
        List<NetworkNode> nodes = nodeMapper.selectList(null);
        List<NetworkLink> links = linkMapper.selectList(null);

        // 调用BGP算法
        BGPAlgorithm.RouteResult result = bgpAlgorithm.calculateBGPRoute(
                request.getSourceNodeId(),
                request.getDestNodeId(),
                nodes,
                links
        );

        // 检查路径是否可达
        if (result.getPath() == null || result.getPath().isEmpty() ||
            result.getTotalWeight() == Integer.MAX_VALUE) {
            throw new BusinessException(ResultCode.PATH_NOT_REACHABLE);
        }

        // 转换为响应对象
        return buildRouteResponse("BGP", request, result.getPath(), result.getTotalWeight(),
                result.getHopCount(), result.getPathDetails());
    }

    /**
     * 构建路由响应对象（通用方法）
     */
    private <T> RouteResponse buildRouteResponse(String algorithm, RouteCalculateRequest request,
                                                  List<String> path, Integer totalWeight,
                                                  Integer hopCount, List<T> pathDetails) {
        RouteResponse response = new RouteResponse();
        response.setAlgorithm(algorithm);
        response.setSourceNodeId(request.getSourceNodeId());
        response.setDestNodeId(request.getDestNodeId());
        response.setPath(path);
        response.setTotalWeight(totalWeight);
        response.setHopCount(hopCount);

        // 计算总延迟
        int totalDelay = 0;
        List<RouteResponse.PathDetail> responseDetails = new java.util.ArrayList<>();

        for (T detail : pathDetails) {
            RouteResponse.PathDetail pd = new RouteResponse.PathDetail();
            BeanUtils.copyProperties(detail, pd);
            responseDetails.add(pd);
            totalDelay += pd.getDelay();
        }

        response.setTotalDelay(totalDelay);
        response.setPathDetails(responseDetails);

        return response;
    }
}
