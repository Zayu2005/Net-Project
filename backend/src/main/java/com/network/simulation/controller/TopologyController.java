package com.network.simulation.controller;

import com.network.simulation.common.result.Result;
import com.network.simulation.dto.response.TopologyResponse;
import com.network.simulation.service.ITopologyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 拓扑控制器
 *
 * @author Network Simulation Team
 */
@Tag(name = "拓扑管理", description = "网络拓扑分析接口")
@RestController
@RequestMapping("/api/v1/topology")
@RequiredArgsConstructor
public class TopologyController {

    private final ITopologyService topologyService;

    @Operation(summary = "获取网络拓扑", description = "获取完整的网络拓扑结构")
    @GetMapping
    public Result<TopologyResponse> getTopology() {
        return Result.success(topologyService.getTopology());
    }

    @Operation(summary = "分析连通性", description = "分析网络拓扑的连通性")
    @GetMapping("/connectivity")
    public Result<TopologyResponse.ConnectivityAnalysis> analyzeConnectivity() {
        return Result.success(topologyService.analyzeConnectivity());
    }

    @Operation(summary = "分析关键节点", description = "分析网络拓扑中的关键节点")
    @GetMapping("/critical-nodes")
    public Result<TopologyResponse.CriticalNodesAnalysis> analyzeCriticalNodes() {
        return Result.success(topologyService.analyzeCriticalNodes());
    }
}
