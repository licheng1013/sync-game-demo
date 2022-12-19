package com.aiwan;

import com.aiwan.config.MyUserHook;
import com.aiwan.service.SpringLogicServer;
import com.iohao.game.action.skeleton.ext.spring.ActionFactoryBeanForSpring;
import com.iohao.game.bolt.broker.client.external.ExternalServer;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.simple.SimpleRunOne;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lc
 * @since 2022/9/8
 */
@SpringBootApplication
@Slf4j
public class GameApp {
    public static void main(String[] args) {
        // 设置 json 编解码。
        //IoGameGlobalSetting.me().setDataCodec(new JsonDataCodec());

        // 设置 用户钩子接口，用户上线时、下线时会触发
        UserSessions.me().setUserHook(new MyUserHook());

        SpringApplication.run(GameApp.class, args);
        // 游戏对外服端口
        int port = 10100;
        // spring 逻辑服
        var demoLogicServer = new SpringLogicServer();
        SimpleRunOne one = new SimpleRunOne();
        one.setExternalServer(createExternalServer(port)).setLogicServerList(List.of(demoLogicServer)).startup();
    }

    private static ExternalServer createExternalServer(int port) { //开启心跳功能，有时候退出并没有那么快
        return ExternalServer.newBuilder(port)// 游戏对外服 - 构建器，设置并构建
                //.enableIdle()// 开启心跳机制
                .build(); // 构建对外服
    }

    @Bean
    public ActionFactoryBeanForSpring actionFactoryBean() {
        // 将业务框架交给 spring 管理
        return ActionFactoryBeanForSpring.me();
    }
}
