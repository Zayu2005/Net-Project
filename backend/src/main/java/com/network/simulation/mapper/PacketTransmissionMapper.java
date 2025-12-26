package com.network.simulation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.network.simulation.entity.PacketTransmission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据包传输Mapper
 *
 * @author Network Simulation Team
 */
@Mapper
public interface PacketTransmissionMapper extends BaseMapper<PacketTransmission> {
}
