package com.network.simulation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 网络链路实体类
 *
 * @author Network Simulation Team
 */
@Data
@TableName("network_link")
public class NetworkLink implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
    private String targetNodeId;

    /**
     * 链路权重/成本
     */
    private Integer weight;

    /**
     * 带宽 (Mbps)
     */
    private Integer bandwidth;

    /**
     * 延迟 (ms)
     */
    private Integer delay;

    /**
     * 丢包率 (%)
     */
    private BigDecimal packetLossRate;

    /**
     * 链路类型 (ETHERNET/WIRELESS/FIBER)
     */
    private String linkType;

    /**
     * 状态 (0-断开, 1-连接)
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
