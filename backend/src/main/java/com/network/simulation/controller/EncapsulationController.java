package com.network.simulation.controller;

import com.network.simulation.common.result.Result;
import com.network.simulation.dto.request.EncapsulationRequest;
import com.network.simulation.dto.response.EncapsulationResponse;
import com.network.simulation.service.IEncapsulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 协议栈封装控制器
 *
 * @author Network Simulation Team
 */
@Tag(name = "协议栈封装", description = "OSI七层协议栈封装/解封装接口")
@RestController
@RequestMapping("/api/v1/encapsulation")
@RequiredArgsConstructor
public class EncapsulationController {

    private final IEncapsulationService encapsulationService;

    @Operation(summary = "执行协议栈封装", description = "对数据执行OSI七层协议栈封装")
    @PostMapping
    public Result<EncapsulationResponse> encapsulate(
            @Validated @RequestBody EncapsulationRequest request) {
        return Result.success(encapsulationService.encapsulate(request));
    }

    @Operation(summary = "执行协议栈解封装", description = "对数据执行OSI七层协议栈解封装")
    @PostMapping("/decapsulate/{transmissionId}")
    public Result<EncapsulationResponse> decapsulate(
            @Parameter(description = "传输ID", required = true)
            @PathVariable String transmissionId) {
        return Result.success(encapsulationService.decapsulate(transmissionId));
    }

    @Operation(summary = "获取协议层详情", description = "获取指定传输的协议层详细信息")
    @GetMapping("/{transmissionId}")
    public Result<EncapsulationResponse> getLayerDetails(
            @Parameter(description = "传输ID", required = true)
            @PathVariable String transmissionId) {
        return Result.success(encapsulationService.getLayerDetails(transmissionId));
    }
}
