package com.network.simulation.controller;

import com.network.simulation.common.result.Result;
import com.network.simulation.dto.request.TransmissionStartRequest;
import com.network.simulation.dto.response.TransmissionResponse;
import com.network.simulation.service.ITransmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 传输控制器
 *
 * @author Network Simulation Team
 */
@Tag(name = "数据传输", description = "数据包传输模拟接口")
@RestController
@RequestMapping("/api/v1/transmission")
@RequiredArgsConstructor
public class TransmissionController {

    private final ITransmissionService transmissionService;

    @Operation(summary = "启动传输", description = "启动数据包传输模拟")
    @PostMapping("/start")
    public Result<TransmissionResponse> startTransmission(
            @Validated @RequestBody TransmissionStartRequest request) {
        return Result.success(transmissionService.startTransmission(request));
    }

    @Operation(summary = "获取传输历史", description = "获取所有传输历史记录")
    @GetMapping("/history")
    public Result<List<TransmissionResponse>> getHistory() {
        return Result.success(transmissionService.getHistory());
    }

    @Operation(summary = "获取传输状态", description = "根据传输ID获取传输状态和详情")
    @GetMapping("/{transmissionId}")
    public Result<TransmissionResponse> getTransmissionStatus(
            @Parameter(description = "传输ID", required = true)
            @PathVariable String transmissionId) {
        return Result.success(transmissionService.getTransmissionStatus(transmissionId));
    }
}
