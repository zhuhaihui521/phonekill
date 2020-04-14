package com.huitop.secondkill.config;

import com.huitop.secondkill.zookeeper.ZookeeperWatcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author zhuhaihui
 * @date 2020-04-14 14:15
 */
@Configuration
public class ZookeeperConfig {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);

    @Value("${zookeeper.address}")
    private String connectString;

    @Value("${zookeeper.timeout}")
    private int timeout;

    @Bean(name = "zkClient")
    public ZooKeeper zkClient() throws IOException {
        ZookeeperWatcher zookeeperWatcher=new ZookeeperWatcher();
        ZooKeeper zooKeeper=new ZooKeeper(connectString,timeout,zookeeperWatcher);
        zookeeperWatcher.setZooKeeper(zooKeeper);
        return zooKeeper;
    }

}
