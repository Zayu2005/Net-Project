package com.network.simulation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 链路创建请求
 *
 * @author Network Simulation Team
 */
@Data
public class LinkCreateRequest {

    private String sourceNodeId;
    @JsonProperty("destNodeId")
    private String targetNodeId;
    private Integer weight;
    private Integer bandwidth;
    private Integer delay;
    @JsonProperty("lossRate")
    private BigDecimal packetLossRate;
    private String linkType;  // ETHERNET/WIRELESS/FIBER
    private String status;  // UP/DOWN
}
