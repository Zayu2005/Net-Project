package com.network.simulation.controller;

import com.network.simulation.common.result.Result;
import com.network.simulation.dto.response.StatisticsResponse;
import com.network.simulation.service.IStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 统计控制器
 *
 * @author Network Simulation Team
 */
@Tag(name = "统计分析", description = "传输统计与性能分析接口")
@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final IStatisticsService statisticsService;

    @Operation(summary = "获取传输统计", description = "获取指定时间段内的传输统计信息")
    @GetMapping("/transmission")
    public Result<StatisticsResponse.TransmissionStats> getTransmissionStats(
            @Parameter(description = "开始时间")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(statisticsService.getTransmissionStats(startTime, endTime));
    }

    @Operation(summary = "获取算法统计", description = "获取指定时间段内各路由算法的使用统计")
    @GetMapping("/algorithm")
    public Result<StatisticsResponse.AlgorithmStats> getAlgorithmStats(
            @Parameter(description = "开始时间")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(statisticsService.getAlgorithmStats(startTime, endTime));
    }

    @Operation(summary = "获取性能统计", description = "获取指定时间段内的网络性能统计")
    @GetMapping("/performance")
    public Result<StatisticsResponse.PerformanceStats> getPerformanceStats(
            @Parameter(description = "开始时间")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(statisticsService.getPerformanceStats(startTime, endTime));
    }

    @Operation(summary = "获取综合统计", description = "获取指定时间段内的综合统计报告")
    @GetMapping("/comprehensive")
    public Result<StatisticsResponse> getComprehensiveStats(
            @Parameter(description = "开始时间")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(statisticsService.getComprehensiveStats(startTime, endTime));
    }
}
