package com.network.simulation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 网络节点实体类
 *
 * @author Network Simulation Team
 */
@Data
@TableName("network_node")
public class NetworkNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 节点标识符
     */
    private String nodeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * MAC地址
     */
    private String macAddress;

    /**
     * X坐标
     */
    private BigDecimal positionX;

    /**
     * Y坐标
     */
    private BigDecimal positionY;

    /**
     * 节点类型 (ROUTER/SWITCH/HOST)
     */
    private String nodeType;

    /**
     * 状态 (0-离线, 1-在线)
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
