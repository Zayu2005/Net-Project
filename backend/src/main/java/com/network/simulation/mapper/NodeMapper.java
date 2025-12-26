package com.network.simulation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.network.simulation.entity.NetworkNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网络节点Mapper
 *
 * @author Network Simulation Team
 */
@Mapper
public interface NodeMapper extends BaseMapper<NetworkNode> {
}
