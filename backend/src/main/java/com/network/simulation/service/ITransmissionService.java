package com.network.simulation.service;

import com.network.simulation.dto.request.TransmissionStartRequest;
import com.network.simulation.dto.response.TransmissionResponse;

import java.util.List;

/**
 * 传输服务接口
 *
 * @author Network Simulation Team
 */
public interface ITransmissionService {

    /**
     * 启动数据包传输
     */
    TransmissionResponse startTransmission(TransmissionStartRequest request);

    /**
     * 获取传输历史
     */
    List<TransmissionResponse> getHistory();

    /**
     * 获取传输状态
     */
    TransmissionResponse getTransmissionStatus(String transmissionId);
}
