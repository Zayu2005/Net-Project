package com.network.simulation.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 协议栈封装请求
 *
 * @author Network Simulation Team
 */
@Data
public class EncapsulationRequest implements Serializable {

    /**
     * 传输ID
     */
    @NotBlank(message = "传输ID不能为空")
    private String transmissionId;

    /**
     * 协议类型
     */
    @NotBlank(message = "协议类型不能为空")
    private String protocol;

    /**
     * 数据内容
     */
    @NotBlank(message = "数据内容不能为空")
    private String dataContent;
}
