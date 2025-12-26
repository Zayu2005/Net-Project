package com.network.simulation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.network.simulation.common.exception.BusinessException;
import com.network.simulation.common.result.ResultCode;
import com.network.simulation.dto.request.RouteCalculateRequest;
import com.network.simulation.dto.request.TransmissionStartRequest;
import com.network.simulation.dto.response.RouteResponse;
import com.network.simulation.dto.response.TransmissionResponse;
import com.network.simulation.entity.PacketTransmission;
import com.network.simulation.mapper.PacketTransmissionMapper;
import com.network.simulation.service.IRoutingService;
import com.network.simulation.service.ITransmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 传输服务实现
 *
 * @author Network Simulation Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransmissionServiceImpl implements ITransmissionService {

    private final PacketTransmissionMapper transmissionMapper;
    private final IRoutingService routingService;
    private final ObjectMapper objectMapper;

    @Override
    public TransmissionResponse startTransmission(TransmissionStartRequest request) {
        try {
            // 1. 计算路由路径
            RouteCalculateRequest routeRequest = new RouteCalculateRequest();
            routeRequest.setSourceNodeId(request.getSourceNodeId());
            routeRequest.setDestNodeId(request.getDestNodeId());

            RouteResponse routeResponse;
            String algorithm = request.getRoutingAlgorithm().toUpperCase();

            switch (algorithm) {
                case "OSPF":
                    routeResponse = routingService.calculateOSPF(routeRequest);
                    break;
                case "RIP":
                    routeResponse = routingService.calculateRIP(routeRequest);
                    break;
                case "BGP":
                    routeResponse = routingService.calculateBGP(routeRequest);
                    break;
                case "DIJKSTRA":
                default:
                    routeResponse = routingService.calculateDijkstra(routeRequest);
                    break;
            }

            // 2. 创建传输记录
            PacketTransmission transmission = new PacketTransmission();
            transmission.setTransmissionId(UUID.randomUUID().toString());
            transmission.setSourceNodeId(request.getSourceNodeId());
            transmission.setDestNodeId(request.getDestNodeId());
            transmission.setProtocol(request.getProtocol());
            transmission.setDataContent(request.getDataContent());
            transmission.setDataSize(request.getDataContent() != null ?
                    request.getDataContent().getBytes().length : 0);
            transmission.setRoutingAlgorithm(algorithm);
            transmission.setRoutingPath(objectMapper.writeValueAsString(routeResponse.getPath()));
            transmission.setHopCount(routeResponse.getHopCount());
            transmission.setTotalDelay(routeResponse.getTotalDelay());
            transmission.setStatus("SUCCESS");
            transmission.setStartTime(LocalDateTime.now());
            transmission.setEndTime(LocalDateTime.now());

            transmissionMapper.insert(transmission);

            // 3. 转换为响应对象
            TransmissionResponse response = new TransmissionResponse();
            BeanUtils.copyProperties(transmission, response);
            response.setTimestamp(transmission.getStartTime());
            response.setPath(String.join(" -> ", routeResponse.getPath()));
            return response;

        } catch (Exception e) {
            log.error("传输失败", e);
            throw new BusinessException(ResultCode.TRANSMISSION_FAILED);
        }
    }

    @Override
    public List<TransmissionResponse> getHistory() {
        LambdaQueryWrapper<PacketTransmission> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(PacketTransmission::getCreatedTime);
        List<PacketTransmission> transmissions = transmissionMapper.selectList(wrapper);

        return transmissions.stream()
                .map(transmission -> {
                    TransmissionResponse response = new TransmissionResponse();
                    BeanUtils.copyProperties(transmission, response);

                    // 设置时间戳
                    response.setTimestamp(transmission.getStartTime());

                    // 解析路由路径并转换为显示格式
                    try {
                        if (transmission.getRoutingPath() != null) {
                            List<String> pathList = objectMapper.readValue(
                                transmission.getRoutingPath(),
                                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
                            );
                            response.setPath(String.join(" -> ", pathList));
                        }
                    } catch (Exception e) {
                        log.error("解析路由路径失败", e);
                    }

                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public TransmissionResponse getTransmissionStatus(String transmissionId) {
        LambdaQueryWrapper<PacketTransmission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PacketTransmission::getTransmissionId, transmissionId);
        PacketTransmission transmission = transmissionMapper.selectOne(wrapper);

        if (transmission == null) {
            throw new BusinessException(ResultCode.TRANSMISSION_NOT_FOUND);
        }

        TransmissionResponse response = new TransmissionResponse();
        BeanUtils.copyProperties(transmission, response);

        // 设置时间戳
        response.setTimestamp(transmission.getStartTime());

        // 解析路由路径并转换为显示格式
        try {
            if (transmission.getRoutingPath() != null) {
                List<String> pathList = objectMapper.readValue(
                    transmission.getRoutingPath(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
                );
                response.setPath(String.join(" -> ", pathList));
            }
        } catch (Exception e) {
            log.error("解析路由路径失败", e);
        }

        return response;
    }
}
