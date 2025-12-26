package com.network.simulation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据包跳转记录实体类
 *
 * @author Network Simulation Team
 */
@Data
@TableName("packet_hop")
public class PacketHop implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 传输ID
     */
    private String transmissionId;

    /**
     * 跳序号
     */
    private Integer hopSequence;

    /**
     * 当前节点ID
     */
    private String currentNodeId;

    /**
     * 下一跳节点ID
     */
    private String nextNodeId;

    /**
     * 动作 (FORWARD/DELIVER/DROP)
     */
    private String action;

    /**
     * TTL值
     */
    private Integer ttl;

    /**
     * 本跳延迟 (ms)
     */
    private Integer delay;

    /**
     * 封装数据详情 (JSON)
     */
    private String encapsulationData;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}
