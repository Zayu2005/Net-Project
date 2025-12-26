package com.network.simulation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.network.simulation.common.exception.BusinessException;
import com.network.simulation.common.result.ResultCode;
import com.network.simulation.dto.response.ConfigResponse;
import com.network.simulation.entity.SystemConfig;
import com.network.simulation.mapper.SystemConfigMapper;
import com.network.simulation.service.IConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统配置服务实现
 *
 * @author Network Simulation Team
 */
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements IConfigService {

    private final SystemConfigMapper configMapper;

    @Override
    public List<ConfigResponse> getAllConfigs() {
        List<SystemConfig> configs = configMapper.selectList(null);
        return configs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ConfigResponse getConfigByKey(String configKey) {
        SystemConfig config = getConfigOrThrow(configKey);
        return convertToResponse(config);
    }

    @Override
    public void updateConfig(String configKey, String configValue) {
        SystemConfig config = getConfigOrThrow(configKey);
        config.setConfigValue(configValue);
        configMapper.updateById(config);
    }

    @Override
    public void batchUpdateConfigs(List<ConfigResponse> configs) {
        for (ConfigResponse configResponse : configs) {
            updateConfig(configResponse.getConfigKey(), configResponse.getConfigValue());
        }
    }

    /**
     * 根据configKey获取配置，不存在则抛异常
     */
    private SystemConfig getConfigOrThrow(String configKey) {
        LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemConfig::getConfigKey, configKey);
        SystemConfig config = configMapper.selectOne(wrapper);

        if (config == null) {
            throw new BusinessException(ResultCode.CONFIG_NOT_FOUND);
        }
        return config;
    }

    /**
     * 转换为响应对象
     */
    private ConfigResponse convertToResponse(SystemConfig config) {
        ConfigResponse response = new ConfigResponse();
        BeanUtils.copyProperties(config, response);
        return response;
    }
}
