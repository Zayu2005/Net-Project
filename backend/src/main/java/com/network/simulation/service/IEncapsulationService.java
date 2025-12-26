package com.network.simulation.service;

import com.network.simulation.dto.request.EncapsulationRequest;
import com.network.simulation.dto.response.EncapsulationResponse;

/**
 * OSI协议栈封装服务接口
 *
 * @author Network Simulation Team
 */
public interface IEncapsulationService {

    /**
     * 执行OSI七层协议栈封装
     */
    EncapsulationResponse encapsulate(EncapsulationRequest request);

    /**
     * 执行OSI七层协议栈解封装
     */
    EncapsulationResponse decapsulate(String transmissionId);

    /**
     * 获取指定传输的协议层详情
     */
    EncapsulationResponse getLayerDetails(String transmissionId);
}
