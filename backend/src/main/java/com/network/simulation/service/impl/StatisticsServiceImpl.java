package com.network.simulation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.network.simulation.dto.response.StatisticsResponse;
import com.network.simulation.entity.PacketTransmission;
import com.network.simulation.mapper.PacketTransmissionMapper;
import com.network.simulation.service.IStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计服务实现
 *
 * @author Network Simulation Team
 */
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements IStatisticsService {

    private final PacketTransmissionMapper transmissionMapper;

    @Override
    public StatisticsResponse.TransmissionStats getTransmissionStats(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<PacketTransmission> wrapper = new LambdaQueryWrapper<>();
        if (startTime != null) {
            wrapper.ge(PacketTransmission::getStartTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(PacketTransmission::getStartTime, endTime);
        }

        List<PacketTransmission> transmissions = transmissionMapper.selectList(wrapper);

        StatisticsResponse.TransmissionStats stats = new StatisticsResponse.TransmissionStats();
        stats.setTotalTransmissions(transmissions.size());
        stats.setSuccessfulTransmissions((int) transmissions.stream()
                .filter(t -> "SUCCESS".equals(t.getStatus()))
                .count());
        stats.setFailedTransmissions((int) transmissions.stream()
                .filter(t -> "FAILED".equals(t.getStatus()))
                .count());
        stats.setAverageHopCount(transmissions.stream()
                .mapToInt(PacketTransmission::getHopCount)
                .average()
                .orElse(0.0));
        stats.setAverageDelay(transmissions.stream()
                .mapToInt(PacketTransmission::getTotalDelay)
                .average()
                .orElse(0.0));
        stats.setTotalDataTransferred(transmissions.stream()
                .mapToLong(PacketTransmission::getDataSize)
                .sum());

        return stats;
    }

    @Override
    public StatisticsResponse.AlgorithmStats getAlgorithmStats(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<PacketTransmission> wrapper = new LambdaQueryWrapper<>();
        if (startTime != null) {
            wrapper.ge(PacketTransmission::getStartTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(PacketTransmission::getStartTime, endTime);
        }

        List<PacketTransmission> transmissions = transmissionMapper.selectList(wrapper);

        // 统计各算法使用次数
        Map<String, Long> algorithmUsage = transmissions.stream()
                .collect(Collectors.groupingBy(PacketTransmission::getRoutingAlgorithm, Collectors.counting()));

        // 统计各算法平均延迟
        Map<String, Double> algorithmAvgDelay = transmissions.stream()
                .collect(Collectors.groupingBy(
                        PacketTransmission::getRoutingAlgorithm,
                        Collectors.averagingInt(PacketTransmission::getTotalDelay)
                ));

        // 统计各算法平均跳数
        Map<String, Double> algorithmAvgHops = transmissions.stream()
                .collect(Collectors.groupingBy(
                        PacketTransmission::getRoutingAlgorithm,
                        Collectors.averagingInt(PacketTransmission::getHopCount)
                ));

        StatisticsResponse.AlgorithmStats stats = new StatisticsResponse.AlgorithmStats();
        stats.setAlgorithmUsage(algorithmUsage);
        stats.setAlgorithmAvgDelay(algorithmAvgDelay);
        stats.setAlgorithmAvgHops(algorithmAvgHops);

        return stats;
    }

    @Override
    public StatisticsResponse.PerformanceStats getPerformanceStats(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<PacketTransmission> wrapper = new LambdaQueryWrapper<>();
        if (startTime != null) {
            wrapper.ge(PacketTransmission::getStartTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(PacketTransmission::getStartTime, endTime);
        }

        List<PacketTransmission> transmissions = transmissionMapper.selectList(wrapper);

        StatisticsResponse.PerformanceStats stats = new StatisticsResponse.PerformanceStats();
        stats.setMinDelay(transmissions.stream()
                .mapToInt(PacketTransmission::getTotalDelay)
                .min()
                .orElse(0));
        stats.setMaxDelay(transmissions.stream()
                .mapToInt(PacketTransmission::getTotalDelay)
                .max()
                .orElse(0));
        stats.setAvgDelay(transmissions.stream()
                .mapToInt(PacketTransmission::getTotalDelay)
                .average()
                .orElse(0.0));
        stats.setMinHopCount(transmissions.stream()
                .mapToInt(PacketTransmission::getHopCount)
                .min()
                .orElse(0));
        stats.setMaxHopCount(transmissions.stream()
                .mapToInt(PacketTransmission::getHopCount)
                .max()
                .orElse(0));
        stats.setAvgHopCount(transmissions.stream()
                .mapToInt(PacketTransmission::getHopCount)
                .average()
                .orElse(0.0));

        // 计算吞吐量（假设时间段内的总数据量）
        if (startTime != null && endTime != null) {
            long totalBytes = transmissions.stream()
                    .mapToLong(PacketTransmission::getDataSize)
                    .sum();
            long durationSeconds = java.time.Duration.between(startTime, endTime).getSeconds();
            stats.setThroughput(durationSeconds > 0 ? (double) totalBytes / durationSeconds : 0.0);
        }

        return stats;
    }

    @Override
    public StatisticsResponse getComprehensiveStats(LocalDateTime startTime, LocalDateTime endTime) {
        StatisticsResponse response = new StatisticsResponse();
        response.setTransmissionStats(getTransmissionStats(startTime, endTime));
        response.setAlgorithmStats(getAlgorithmStats(startTime, endTime));
        response.setPerformanceStats(getPerformanceStats(startTime, endTime));
        response.setStartTime(startTime);
        response.setEndTime(endTime);

        return response;
    }
}
