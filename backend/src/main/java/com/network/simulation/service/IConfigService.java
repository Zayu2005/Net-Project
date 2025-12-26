package com.network.simulation.service;

import com.network.simulation.dto.response.ConfigResponse;

import java.util.List;

/**
 * 系统配置服务接口
 *
 * @author Network Simulation Team
 */
public interface IConfigService {

    /**
     * 获取所有配置
     */
    List<ConfigResponse> getAllConfigs();

    /**
     * 根据配置键获取配置
     */
    ConfigResponse getConfigByKey(String configKey);

    /**
     * 更新配置
     */
    void updateConfig(String configKey, String configValue);

    /**
     * 批量更新配置
     */
    void batchUpdateConfigs(List<ConfigResponse> configs);
}
