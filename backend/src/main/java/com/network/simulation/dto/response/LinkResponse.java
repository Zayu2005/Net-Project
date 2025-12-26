package com.network.simulation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 链路响应
 *
 * @author Network Simulation Team
 */
@Data
public class LinkResponse implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 链路标识符
     */
    private String linkId;

    /**
     * 源节点ID
     */
    private String sourceNodeId;

    /**
     * 目标节点ID
     */
    @JsonProperty("destNodeId")
    private String targetNodeId;

    /**
     * 链路权重
     */
    private Integer weight;

    /**
     * 带宽（Mbps）
     */
    private Integer bandwidth;

    /**
     * 延迟（ms）
     */
    private Integer delay;

    /**
     * 丢包率
     */
    @JsonProperty("lossRate")
    private BigDecimal packetLossRate;

    /**
     * 状态（UP-在线 DOWN-离线）
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}
