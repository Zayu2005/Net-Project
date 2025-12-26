package com.network.simulation.dto.request;

import lombok.Data;

/**
 * 传输启动请求
 *
 * @author Network Simulation Team
 */
@Data
public class TransmissionStartRequest {

    private String sourceNodeId;
    private String destNodeId;
    private String protocol;  // TCP/UDP/ICMP
    private String dataContent;
    private String routingAlgorithm;  // DIJKSTRA/OSPF/RIP/BGP
    private Integer sourcePort;
    private Integer destPort;
}
