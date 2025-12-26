package com.network.simulation.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 协议栈封装响应
 *
 * @author Network Simulation Team
 */
@Data
public class EncapsulationResponse implements Serializable {

    /**
     * 传输ID
     */
    private String transmissionId;

    /**
     * 协议层详情列表
     */
    private List<LayerDetail> layers;

    /**
     * 总大小（字节）
     */
    private Integer totalSize;

    /**
     * 协议层详情
     */
    @Data
    public static class LayerDetail implements Serializable {

        /**
         * 层号（1-7）
         */
        private Integer layerNumber;

        /**
         * 层名称
         */
        private String layerName;

        /**
         * 头部数据
         */
        private Map<String, Object> headerData;

        /**
         * 负载大小（字节）
         */
        private Integer payloadSize;
    }
}
