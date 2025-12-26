package com.network.simulation.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 统计响应
 *
 * @author Network Simulation Team
 */
@Data
public class StatisticsResponse implements Serializable {

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 传输统计
     */
    private TransmissionStats transmissionStats;

    /**
     * 算法统计
     */
    private AlgorithmStats algorithmStats;

    /**
     * 性能统计
     */
    private PerformanceStats performanceStats;

    /**
     * 传输统计
     */
    @Data
    public static class TransmissionStats implements Serializable {

        /**
         * 总传输次数
         */
        private Integer totalTransmissions;

        /**
         * 成功传输次数
         */
        private Integer successfulTransmissions;

        /**
         * 失败传输次数
         */
        private Integer failedTransmissions;

        /**
         * 平均跳数
         */
        private Double averageHopCount;

        /**
         * 平均延迟（ms）
         */
        private Double averageDelay;

        /**
         * 总传输数据量（字节）
         */
        private Long totalDataTransferred;
    }

    /**
     * 算法统计
     */
    @Data
    public static class AlgorithmStats implements Serializable {

        /**
         * 各算法使用次数
         */
        private Map<String, Long> algorithmUsage;

        /**
         * 各算法平均延迟
         */
        private Map<String, Double> algorithmAvgDelay;

        /**
         * 各算法平均跳数
         */
        private Map<String, Double> algorithmAvgHops;
    }

    /**
     * 性能统计
     */
    @Data
    public static class PerformanceStats implements Serializable {

        /**
         * 最小延迟（ms）
         */
        private Integer minDelay;

        /**
         * 最大延迟（ms）
         */
        private Integer maxDelay;

        /**
         * 平均延迟（ms）
         */
        private Double avgDelay;

        /**
         * 最小跳数
         */
        private Integer minHopCount;

        /**
         * 最大跳数
         */
        private Integer maxHopCount;

        /**
         * 平均跳数
         */
        private Double avgHopCount;

        /**
         * 吞吐量（字节/秒）
         */
        private Double throughput;
    }
}
