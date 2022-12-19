package com.aiwan.service;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilderParamConfig;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.common.kit.NetworkKit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringLogicServer extends AbstractBrokerClientStartup {

    @Override
    public void startupSuccess(BrokerClient brokerClient) {
        super.startupSuccess(brokerClient);
        // 设置 用户钩子接口，用户上线时、下线时会触发
    }

    @Override
    public BarSkeleton createBarSkeleton() {
        // 业务框架构建器 配置
        var config = new BarSkeletonBuilderParamConfig().scanActionPackage(SyncAction.class);
        var builder = config.createBuilder(); // 业务框架构建器
        return builder.build();
    }

    @Override
    public BrokerClientBuilder createBrokerClientBuilder() {
        BrokerClientBuilder builder = BrokerClient.newBuilder();
        builder.appName("demo spring 游戏逻辑服");
        return builder;
    }

    @Override
    public BrokerAddress createBrokerAddress() {
        // 类似 127.0.0.1 ，但这里是本机的 ip
        String localIp = NetworkKit.LOCAL_IP;
        int brokerPort = IoGameGlobalConfig.brokerPort;
        return new BrokerAddress(localIp, brokerPort);
    }
}