package com.network.simulation.common.result;

import lombok.Getter;

/**
 * 状态码枚举
 *
 * @author Network Simulation Team
 */
@Getter
public enum ResultCode {

    // 通用状态码
    SUCCESS(200, "success"),
    ERROR(500, "error"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    NOT_FOUND(404, "资源不存在"),

    // 节点相关错误码 (1001-1099)
    NODE_NOT_FOUND(1001, "节点不存在"),
    NODE_ALREADY_EXISTS(1002, "节点已存在"),
    NODE_DELETE_FAILED(1003, "节点删除失败"),

    // 链路相关错误码 (1101-1199)
    LINK_NOT_FOUND(1101, "链路不存在"),
    LINK_ALREADY_EXISTS(1102, "链路已存在"),
    LINK_DELETE_FAILED(1103, "链路删除失败"),

    // 路由相关错误码 (1201-1299)
    PATH_NOT_REACHABLE(1201, "路径不可达"),
    ROUTING_FAILED(1202, "路由计算失败"),
    ROUTING_TABLE_EMPTY(1203, "路由表为空"),

    // 传输相关错误码 (1301-1399)
    TRANSMISSION_TIMEOUT(1301, "传输超时"),
    PACKET_LOST(1302, "数据包丢失"),
    TRANSMISSION_FAILED(1303, "传输失败"),
    TRANSMISSION_NOT_FOUND(1304, "传输记录不存在"),

    // 封装相关错误码 (1401-1499)
    ENCAPSULATION_FAILED(1401, "封装失败"),
    DECAPSULATION_FAILED(1402, "解封装失败"),
    PROTOCOL_LAYER_NOT_FOUND(1403, "协议层不存在"),

    // 配置相关错误码 (1501-1599)
    CONFIG_NOT_FOUND(1501, "配置不存在"),
    CONFIG_UPDATE_FAILED(1502, "配置更新失败");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态消息
     */
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
