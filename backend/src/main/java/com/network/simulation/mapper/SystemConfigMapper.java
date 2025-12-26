package com.network.simulation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.network.simulation.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置Mapper
 *
 * @author Network Simulation Team
 */
@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {
}
