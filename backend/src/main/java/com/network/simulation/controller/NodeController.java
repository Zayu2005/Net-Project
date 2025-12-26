package com.network.simulation.controller;

import com.network.simulation.common.result.Result;
import com.network.simulation.dto.request.NodeCreateRequest;
import com.network.simulation.dto.request.NodeUpdateRequest;
import com.network.simulation.dto.response.NodeResponse;
import com.network.simulation.service.INodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 节点控制器
 *
 * @author Network Simulation Team
 */
@Tag(name = "节点管理", description = "网络节点的增删改查接口")
@RestController
@RequestMapping("/api/v1/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final INodeService nodeService;

    @Operation(summary = "获取所有节点", description = "获取系统中所有网络节点列表")
    @GetMapping
    public Result<List<NodeResponse>> getAllNodes() {
        return Result.success(nodeService.getAllNodes());
    }

    @Operation(summary = "获取节点详情", description = "根据节点ID获取节点详细信息")
    @GetMapping("/{nodeId}")
    public Result<NodeResponse> getNodeById(
            @Parameter(description = "节点ID", required = true)
            @PathVariable String nodeId) {
        return Result.success(nodeService.getNodeById(nodeId));
    }

    @Operation(summary = "创建节点", description = "创建新的网络节点")
    @PostMapping
    public Result<NodeResponse> createNode(
            @Validated @RequestBody NodeCreateRequest request) {
        return Result.success(nodeService.createNode(request));
    }

    @Operation(summary = "更新节点", description = "更新现有节点信息")
    @PutMapping("/{nodeId}")
    public Result<Void> updateNode(
            @Parameter(description = "节点ID", required = true)
            @PathVariable String nodeId,
            @Validated @RequestBody NodeUpdateRequest request) {
        nodeService.updateNode(nodeId, request);
        return Result.success(null);
    }

    @Operation(summary = "删除节点", description = "删除指定节点")
    @DeleteMapping("/{nodeId}")
    public Result<Void> deleteNode(
            @Parameter(description = "节点ID", required = true)
            @PathVariable String nodeId) {
        nodeService.deleteNode(nodeId);
        return Result.success(null);
    }
}
