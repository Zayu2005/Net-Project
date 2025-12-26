package com.network.simulation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.network.simulation.entity.NetworkLink;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网络链路Mapper
 *
 * @author Network Simulation Team
 */
@Mapper
public interface LinkMapper extends BaseMapper<NetworkLink> {
}
