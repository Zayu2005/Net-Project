package com.network.simulation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 传输响应
 *
 * @author Network Simulation Team
 */
@Data
public class TransmissionResponse {

    private String transmissionId;
    private String sourceNodeId;
    private String destNodeId;
    private String protocol;
    private String dataContent;
    private Integer dataSize;
    private String routingAlgorithm;
    private String routingPath;
    private Integer hopCount;
    private Integer totalDelay;
    private String status;
    private String errorMessage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 传输时间戳（用于前端显示）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * 路径（用于前端显示，逗号分隔的节点名称）
     */
    private String path;
}
