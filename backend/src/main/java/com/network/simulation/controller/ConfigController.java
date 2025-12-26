package com.network.simulation.controller;

import com.network.simulation.common.result.Result;
import com.network.simulation.dto.response.ConfigResponse;
import com.network.simulation.service.IConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置控制器
 *
 * @author Network Simulation Team
 */
@Tag(name = "系统配置", description = "系统配置管理接口")
@RestController
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
public class ConfigController {

    private final IConfigService configService;

    @Operation(summary = "获取所有配置", description = "获取系统中所有配置项")
    @GetMapping
    public Result<List<ConfigResponse>> getAllConfigs() {
        return Result.success(configService.getAllConfigs());
    }

    @Operation(summary = "获取配置详情", description = "根据配置键获取配置详情")
    @GetMapping("/{configKey}")
    public Result<ConfigResponse> getConfigByKey(
            @Parameter(description = "配置键", required = true)
            @PathVariable String configKey) {
        return Result.success(configService.getConfigByKey(configKey));
    }

    @Operation(summary = "更新配置", description = "更新指定配置项的值")
    @PutMapping("/{configKey}")
    public Result<Void> updateConfig(
            @Parameter(description = "配置键", required = true)
            @PathVariable String configKey,
            @Parameter(description = "配置值", required = true)
            @RequestParam String configValue) {
        configService.updateConfig(configKey, configValue);
        return Result.success(null);
    }

    @Operation(summary = "批量更新配置", description = "批量更新多个配置项")
    @PutMapping("/batch")
    public Result<Void> batchUpdateConfigs(
            @RequestBody List<ConfigResponse> configs) {
        configService.batchUpdateConfigs(configs);
        return Result.success(null);
    }
}
