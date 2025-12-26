package com.network.simulation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 网络模拟系统启动类
 *
 * @author Network Simulation Team
 * @version 1.0.0
 */
@SpringBootApplication
@MapperScan("com.network.simulation.mapper")
public class NetworkSimulationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetworkSimulationApplication.class, args);
        System.out.println("========================================");
        System.out.println("网络模拟系统启动成功!");
        System.out.println("API文档地址:                                                                                     ");
        System.out.println("========================================");
    }
}
