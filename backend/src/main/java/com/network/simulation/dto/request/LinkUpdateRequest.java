package com.network.simulation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 链路更新请求
 *
 * @author Network Simulation Team
 */
@Data
public class LinkUpdateRequest implements Serializable {

    /**
     * 链路权重
     */
    @Positive(message = "链路权重必须为正数")
    private Integer weight;

    /**
     * 带宽（Mbps）
     */
    @Positive(message = "带宽必须为正数")
    private Integer bandwidth;

    /**
     * 延迟（ms）
     */
    private Integer delay;

    /**
     * 丢包率（0-1之间）
     */
    @JsonProperty("lossRate")
    private BigDecimal packetLossRate;

    /**
     * 状态（UP-在线 DOWN-离线）
     */
    private String status;
}
