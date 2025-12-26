package com.network.simulation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 路由表实体类
 *
 * @author Network Simulation Team
 */
@Data
@TableName("routing_table")
public class RoutingTable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 当前节点ID
     */
    private String nodeId;

    /**
     * 目标网络
     */
    private String destinationNetwork;

    /**
     * 下一跳节点ID
     */
    private String nextHop;

    /**
     * 路由度量值
     */
    private Integer metric;

    /**
     * 出接口
     */
    private String interfaceName;

    /**
     * 路由协议 (STATIC/OSPF/RIP/BGP/DIJKSTRA)
     */
    private String protocol;

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
