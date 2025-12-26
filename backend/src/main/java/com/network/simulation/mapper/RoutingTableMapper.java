package com.network.simulation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.network.simulation.entity.RoutingTable;
import org.apache.ibatis.annotations.Mapper;

/**
 * 路由表Mapper
 *
 * @author Network Simulation Team
 */
@Mapper
public interface RoutingTableMapper extends BaseMapper<RoutingTable> {
}
