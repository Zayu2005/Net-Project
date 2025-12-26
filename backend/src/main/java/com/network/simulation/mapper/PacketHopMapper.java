package com.network.simulation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.network.simulation.entity.PacketHop;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据包跳转Mapper
 *
 * @author Network Simulation Team
 */
@Mapper
public interface PacketHopMapper extends BaseMapper<PacketHop> {
}
