package com.network.simulation.controller;

import com.network.simulation.common.result.Result;
import com.network.simulation.dto.request.LinkCreateRequest;
import com.network.simulation.dto.request.LinkUpdateRequest;
import com.network.simulation.dto.response.LinkResponse;
import com.network.simulation.service.ILinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 链路控制器
 *
 * @author Network Simulation Team
 */
@Tag(name = "链路管理", description = "网络链路的增删改查接口")
@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor
public class LinkController {

    private final ILinkService linkService;

    @Operation(summary = "获取所有链路", description = "获取系统中所有网络链路列表")
    @GetMapping
    public Result<List<LinkResponse>> getAllLinks() {
        return Result.success(linkService.getAllLinks());
    }

    @Operation(summary = "获取链路详情", description = "根据链路ID获取链路详细信息")
    @GetMapping("/{linkId}")
    public Result<LinkResponse> getLinkById(
            @Parameter(description = "链路ID", required = true)
            @PathVariable String linkId) {
        return Result.success(linkService.getLinkById(linkId));
    }

    @Operation(summary = "获取节点相关链路", description = "获取指定节点的所有相关链路")
    @GetMapping("/node/{nodeId}")
    public Result<List<LinkResponse>> getLinksByNodeId(
            @Parameter(description = "节点ID", required = true)
            @PathVariable String nodeId) {
        return Result.success(linkService.getLinksByNodeId(nodeId));
    }

    @Operation(summary = "创建链路", description = "创建新的网络链路")
    @PostMapping
    public Result<LinkResponse> createLink(
            @Validated @RequestBody LinkCreateRequest request) {
        return Result.success(linkService.createLink(request));
    }

    @Operation(summary = "更新链路", description = "更新现有链路信息")
    @PutMapping("/{linkId}")
    public Result<Void> updateLink(
            @Parameter(description = "链路ID", required = true)
            @PathVariable String linkId,
            @Validated @RequestBody LinkUpdateRequest request) {
        linkService.updateLink(linkId, request);
        return Result.success(null);
    }

    @Operation(summary = "删除链路", description = "删除指定链路")
    @DeleteMapping("/{linkId}")
    public Result<Void> deleteLink(
            @Parameter(description = "链路ID", required = true)
            @PathVariable String linkId) {
        linkService.deleteLink(linkId);
        return Result.success(null);
    }
}
