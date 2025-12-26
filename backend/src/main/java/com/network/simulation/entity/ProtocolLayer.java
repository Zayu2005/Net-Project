package com.network.simulation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 协议封装层实体类
 *
 * @author Network Simulation Team
 */
@Data
@TableName("protocol_layer")
public class ProtocolLayer implements Serializable {

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
     * 跳记录ID
     */
    private Long hopId;

    /**
     * 层号 (1-7)
     */
    private Integer layerNumber;

    /**
     * 层名称
     */
    private String layerName;

    /**
     * 头部数据 (JSON)
     */
    private String headerData;

    /**
     * 负载大小
     */
    private Integer payloadSize;

    /**
     * 总大小
     */
    private Integer totalSize;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}
