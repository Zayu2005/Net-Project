package com.network.simulation.controller;

import com.network.simulation.common.result.Result;
import com.network.simulation.dto.request.RouteCalculateRequest;
import com.network.simulation.dto.response.RouteResponse;
import com.network.simulation.service.IRoutingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 路由控制器
 *
 * @author Network Simulation Team
 */
@Tag(name = "路由计算", description = "路由算法计算接口")
@RestController
@RequestMapping("/api/v1/routing")
@RequiredArgsConstructor
public class RoutingController {

    private final IRoutingService routingService;

    @Operation(summary = "Dijkstra算法计算", description = "使用Dijkstra算法计算最短路径")
    @PostMapping("/dijkstra")
    public Result<RouteResponse> calculateDijkstra(
            @Validated @RequestBody RouteCalculateRequest request) {
        return Result.success(routingService.calculateDijkstra(request));
    }

    @Operation(summary = "OSPF算法计算", description = "使用OSPF算法计算最短路径")
    @PostMapping("/ospf")
    public Result<RouteResponse> calculateOSPF(
            @Validated @RequestBody RouteCalculateRequest request) {
        return Result.success(routingService.calculateOSPF(request));
    }

    @Operation(summary = "RIP算法计算", description = "使用RIP算法计算最短路径")
    @PostMapping("/rip")
    public Result<RouteResponse> calculateRIP(
            @Validated @RequestBody RouteCalculateRequest request) {
        return Result.success(routingService.calculateRIP(request));
    }

    @Operation(summary = "BGP算法计算", description = "使用BGP算法计算最短路径")
    @PostMapping("/bgp")
    public Result<RouteResponse> calculateBGP(
            @Validated @RequestBody RouteCalculateRequest request) {
        return Result.success(routingService.calculateBGP(request));
    }
}
