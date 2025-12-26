package com.network.simulation.service;

import com.network.simulation.dto.response.StatisticsResponse;

import java.time.LocalDateTime;

/**
 * 统计服务接口
 *
 * @author Network Simulation Team
 */
public interface IStatisticsService {

    /**
     * 获取传输统计信息
     */
    StatisticsResponse.TransmissionStats getTransmissionStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取路由算法使用统计
     */
    StatisticsResponse.AlgorithmStats getAlgorithmStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取网络性能统计
     */
    StatisticsResponse.PerformanceStats getPerformanceStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取综合统计报告
     */
    StatisticsResponse getComprehensiveStats(LocalDateTime startTime, LocalDateTime endTime);
}
