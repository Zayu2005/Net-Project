package com.network.simulation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.network.simulation.common.exception.BusinessException;
import com.network.simulation.common.result.ResultCode;
import com.network.simulation.dto.request.EncapsulationRequest;
import com.network.simulation.dto.response.EncapsulationResponse;
import com.network.simulation.entity.PacketTransmission;
import com.network.simulation.entity.ProtocolLayer;
import com.network.simulation.mapper.PacketTransmissionMapper;
import com.network.simulation.mapper.ProtocolLayerMapper;
import com.network.simulation.service.IEncapsulationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * OSI协议栈封装服务实现
 *
 * @author Network Simulation Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EncapsulationServiceImpl implements IEncapsulationService {

    private final ProtocolLayerMapper protocolLayerMapper;
    private final PacketTransmissionMapper transmissionMapper;
    private final ObjectMapper objectMapper;

    private static final String[] LAYER_NAMES = {
            "",  // 占位，索引从1开始
            "物理层 (Physical Layer)",
            "数据链路层 (Data Link Layer)",
            "网络层 (Network Layer)",
            "传输层 (Transport Layer)",
            "会话层 (Session Layer)",
            "表示层 (Presentation Layer)",
            "应用层 (Application Layer)"
    };

    @Override
    public EncapsulationResponse encapsulate(EncapsulationRequest request) {
        try {
            // 验证传输记录存在
            PacketTransmission transmission = getTransmissionOrThrow(request.getTransmissionId());

            List<ProtocolLayer> layers = new ArrayList<>();

            // 计算各层头部大小
            int transportHeaderSize = getTransportHeaderSize(request.getProtocol());
            int applicationHeaderSize = getApplicationHeaderSize(request.getProtocol());

            // 7. 应用层
            int appPayloadSize = request.getDataContent().getBytes().length + applicationHeaderSize;
            ProtocolLayer layer7 = createLayer(request.getTransmissionId(), 7,
                    createApplicationHeader(request.getProtocol()),
                    appPayloadSize);
            layers.add(layer7);

            // 6. 表示层 - 数据格式转换、加密
            ProtocolLayer layer6 = createLayer(request.getTransmissionId(), 6,
                    createPresentationHeader(),
                    layer7.getPayloadSize());
            layers.add(layer6);

            // 5. 会话层 - 会话管理
            ProtocolLayer layer5 = createLayer(request.getTransmissionId(), 5,
                    createSessionHeader(),
                    layer6.getPayloadSize());
            layers.add(layer5);

            // 4. 传输层 - TCP/UDP
            ProtocolLayer layer4 = createLayer(request.getTransmissionId(), 4,
                    createTransportHeader(request.getProtocol(), transmission),
                    layer5.getPayloadSize());
            layers.add(layer4);

            // 3. 网络层 - IP
            ProtocolLayer layer3 = createLayer(request.getTransmissionId(), 3,
                    createNetworkHeader(transmission),
                    layer4.getPayloadSize() + transportHeaderSize); // 动态传输层头部
            layers.add(layer3);

            // 2. 数据链路层 - MAC
            ProtocolLayer layer2 = createLayer(request.getTransmissionId(), 2,
                    createDataLinkHeader(transmission),
                    layer3.getPayloadSize() + 20); // IP头部固定20字节
            layers.add(layer2);

            // 1. 物理层 - 比特流
            ProtocolLayer layer1 = createLayer(request.getTransmissionId(), 1,
                    createPhysicalHeader(),
                    layer2.getPayloadSize() + 18); // MAC头部14字节 + FCS 4字节
            layers.add(layer1);

            // 批量保存所有层
            layers.forEach(protocolLayerMapper::insert);

            // 构建响应
            EncapsulationResponse response = new EncapsulationResponse();
            response.setTransmissionId(request.getTransmissionId());
            response.setLayers(convertToLayerDetails(layers));
            response.setTotalSize(layer1.getPayloadSize());

            return response;

        } catch (JsonProcessingException e) {
            log.error("协议栈封装失败", e);
            throw new BusinessException(ResultCode.ENCAPSULATION_FAILED);
        }
    }

    @Override
    public EncapsulationResponse decapsulate(String transmissionId) {
        // 获取所有协议层
        LambdaQueryWrapper<ProtocolLayer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProtocolLayer::getTransmissionId, transmissionId)
                .orderByAsc(ProtocolLayer::getLayerNumber);

        List<ProtocolLayer> layers = protocolLayerMapper.selectList(wrapper);
        if (layers.isEmpty()) {
            throw new BusinessException(ResultCode.PROTOCOL_LAYER_NOT_FOUND);
        }

        EncapsulationResponse response = new EncapsulationResponse();
        response.setTransmissionId(transmissionId);
        response.setLayers(convertToLayerDetails(layers));
        response.setTotalSize(layers.get(layers.size() - 1).getPayloadSize());

        return response;
    }

    @Override
    public EncapsulationResponse getLayerDetails(String transmissionId) {
        return decapsulate(transmissionId);
    }

    /**
     * 创建协议层对象
     */
    private ProtocolLayer createLayer(String transmissionId, int layerNumber,
                                       Map<String, Object> headerData, int payloadSize) throws JsonProcessingException {
        ProtocolLayer layer = new ProtocolLayer();
        layer.setTransmissionId(transmissionId);
        layer.setLayerNumber(layerNumber);
        layer.setLayerName(LAYER_NAMES[layerNumber]);
        layer.setHeaderData(objectMapper.writeValueAsString(headerData));
        layer.setPayloadSize(payloadSize);
        return layer;
    }

    /**
     * 创建应用层头部
     */
    private Map<String, Object> createApplicationHeader(String protocol) {
        Map<String, Object> header = new HashMap<>();
        header.put("protocol", protocol.toUpperCase());

        switch (protocol.toUpperCase()) {
            case "HTTP":
                header.put("method", "GET");
                header.put("version", "HTTP/1.1");
                header.put("host", "www.example.com");
                header.put("userAgent", "NetworkSimulator/1.0");
                header.put("accept", "*/*");
                header.put("connection", "keep-alive");
                header.put("headerSize", "~350 bytes");
                break;
            case "TCP":
                header.put("type", "Raw TCP Stream");
                header.put("contentType", "application/octet-stream");
                header.put("encoding", "binary");
                break;
            case "UDP":
                header.put("type", "Raw UDP Datagram");
                header.put("contentType", "application/octet-stream");
                header.put("encoding", "binary");
                break;
            default:
                header.put("contentType", "application/octet-stream");
                header.put("encoding", "UTF-8");
        }

        return header;
    }

    /**
     * 创建表示层头部
     */
    private Map<String, Object> createPresentationHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("compression", "none");
        header.put("encryption", "none");
        header.put("format", "binary");
        return header;
    }

    /**
     * 创建会话层头部
     */
    private Map<String, Object> createSessionHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("sessionId", UUID.randomUUID().toString());
        header.put("synchronization", "full-duplex");
        return header;
    }

    /**
     * 创建传输层头部
     */
    private Map<String, Object> createTransportHeader(String protocol, PacketTransmission transmission) {
        Map<String, Object> header = new HashMap<>();
        String actualProtocol = protocol.equalsIgnoreCase("HTTP") ? "TCP" : protocol.toUpperCase();
        header.put("protocol", actualProtocol);

        int sourcePort = new Random().nextInt(65535 - 1024) + 1024;
        int destPort = protocol.equalsIgnoreCase("HTTP") ? 80 : 443;

        header.put("sourcePort", sourcePort);
        header.put("destPort", destPort);

        switch (actualProtocol) {
            case "TCP":
                // TCP特有字段
                header.put("sequenceNumber", new Random().nextInt(Integer.MAX_VALUE));
                header.put("acknowledgmentNumber", 0);
                header.put("dataOffset", 5); // 5 * 4 = 20字节
                header.put("flags", "SYN");
                header.put("windowSize", 65535);
                header.put("checksum", "0x" + Integer.toHexString(new Random().nextInt(0xFFFF)));
                header.put("urgentPointer", 0);
                header.put("headerSize", "20 bytes");
                break;
            case "UDP":
                // UDP特有字段
                header.put("length", transmission.getDataContent().getBytes().length + 8);
                header.put("checksum", "0x" + Integer.toHexString(new Random().nextInt(0xFFFF)));
                header.put("headerSize", "8 bytes");
                break;
            default:
                header.put("sequenceNumber", new Random().nextInt(Integer.MAX_VALUE));
                header.put("acknowledgment", 0);
                header.put("windowSize", 65535);
        }

        return header;
    }

    /**
     * 创建网络层头部
     */
    private Map<String, Object> createNetworkHeader(PacketTransmission transmission) {
        Map<String, Object> header = new HashMap<>();
        header.put("version", 4);
        header.put("sourceIP", getNodeIP(transmission.getSourceNodeId()));
        header.put("destIP", getNodeIP(transmission.getDestNodeId()));
        header.put("ttl", 64);
        header.put("protocol", transmission.getProtocol());
        return header;
    }

    /**
     * 创建数据链路层头部
     */
    private Map<String, Object> createDataLinkHeader(PacketTransmission transmission) {
        Map<String, Object> header = new HashMap<>();
        header.put("sourceMAC", getNodeMAC(transmission.getSourceNodeId()));
        header.put("destMAC", getNodeMAC(transmission.getDestNodeId()));
        header.put("etherType", "0x0800"); // IPv4
        header.put("fcs", "0xABCDEF");
        return header;
    }

    /**
     * 创建物理层头部
     */
    private Map<String, Object> createPhysicalHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("encoding", "Manchester");
        header.put("signalType", "Electrical");
        header.put("baudRate", 1000000000); // 1Gbps
        return header;
    }

    /**
     * 转换为响应层详情
     */
    private List<EncapsulationResponse.LayerDetail> convertToLayerDetails(List<ProtocolLayer> layers) {
        return layers.stream()
                .map(layer -> {
                    EncapsulationResponse.LayerDetail detail = new EncapsulationResponse.LayerDetail();
                    detail.setLayerNumber(layer.getLayerNumber());
                    detail.setLayerName(layer.getLayerName());
                    try {
                        detail.setHeaderData(objectMapper.readValue(layer.getHeaderData(), Map.class));
                    } catch (JsonProcessingException e) {
                        detail.setHeaderData(new HashMap<>());
                    }
                    detail.setPayloadSize(layer.getPayloadSize());
                    return detail;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取传输记录
     */
    private PacketTransmission getTransmissionOrThrow(String transmissionId) {
        LambdaQueryWrapper<PacketTransmission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PacketTransmission::getTransmissionId, transmissionId);
        PacketTransmission transmission = transmissionMapper.selectOne(wrapper);
        if (transmission == null) {
            throw new BusinessException(ResultCode.TRANSMISSION_NOT_FOUND);
        }
        return transmission;
    }

    /**
     * 获取节点IP（模拟）
     */
    private String getNodeIP(String nodeId) {
        return "192.168.1." + (nodeId.hashCode() % 254 + 1);
    }

    /**
     * 获取节点MAC（模拟）
     */
    private String getNodeMAC(String nodeId) {
        return String.format("00:00:00:00:00:%02X", nodeId.hashCode() % 256);
    }

    /**
     * 获取传输层头部大小
     */
    private int getTransportHeaderSize(String protocol) {
        switch (protocol.toUpperCase()) {
            case "TCP":
                return 20; // TCP基础头部20字节
            case "UDP":
                return 8;  // UDP头部固定8字节
            case "HTTP":
                return 20; // HTTP基于TCP，传输层使用TCP头部
            default:
                return 20; // 默认使用TCP大小
        }
    }

    /**
     * 获取应用层头部大小
     */
    private int getApplicationHeaderSize(String protocol) {
        switch (protocol.toUpperCase()) {
            case "HTTP":
                // HTTP请求头示例大小（GET / HTTP/1.1 + Host + User-Agent + Accept等）
                return 350;
            case "TCP":
            case "UDP":
                // 纯TCP/UDP没有应用层协议头部
                return 0;
            default:
                return 0;
        }
    }
}
