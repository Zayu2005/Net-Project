package com.network.simulation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据包传输记录实体类
 *
 * @author Network Simulation Team
 */
@Data
@TableName("packet_transmission")
public class PacketTransmission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 传输ID (UUID)
     */
    private String transmissionId;

    /**
     * 源节点ID
     */
    private String sourceNodeId;

    /**
     * 目标节点ID
     */
    private String destNodeId;

    /**
     * 传输协议 (TCP/UDP/ICMP)
     */
    private String protocol;

    /**
     * 数据内容
     */
    private String dataContent;

    /**
     * 数据大小 (字节)
     */
    private Integer dataSize;

    /**
     * 路由算法 (DIJKSTRA/OSPF/RIP/BGP)
     */
    private String routingAlgorithm;

    /**
     * 路由路径 (JSON数组)
     */
    private String routingPath;

    /**
     * 跳数
     */
    private Integer hopCount;

    /**
     * 总延迟 (ms)
     */
    private Integer totalDelay;

    /**
     * 传输状态 (SUCCESS/FAILED/TIMEOUT)
     */
    private String status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}
