package com.aiwan;

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;

/**
 * @author lc
 * @since 2022/12/19
 */
@Slf4j
public class Test {

    public static int count = 0;
    public static Long startTime = System.currentTimeMillis();

    public static void main(String[] args) throws URISyntaxException {
        var wsUrl = "ws://localhost:10100/websocket";
        MyWebSocketClient client = new MyWebSocketClient(wsUrl,(v)->{
            Move move = new Move();
            move.setUp(10);
            ExternalMessage externalMessage = ExternalKit.createExternalMessage(ActionRouter.SYNC, ActionRouter.V2, move);
            byte[] bytes = MyWebSocketClient.codec.encode(externalMessage);
            v.send(bytes);
            new Thread(() -> {
                for (int i = 0; i < 10000000; i++) {
                    v.send(bytes);
                }
            }).start();
            return null;
        });


        client.setMessageFunc(byteBuffer -> {
            count+=1;
            // 接收服务器返回的消息
            byte[] dataContent = byteBuffer.array();
            ExternalMessage message = DataCodecKit.decode(dataContent, ExternalMessage.class);
            //log.info("收到消息 ExternalMessage ========== \n{}", message);
            byte[] data = message.getData();
            if (data != null) {
                Move move = MyWebSocketClient.codec.decode(data, Move.class);
              //  log.info("helloReq ========== \n{}", move);
            }
            long endTime = System.currentTimeMillis();
            if (endTime- startTime > 1000) {
                log.info("1秒请求数: {}",count);
                startTime = endTime;
                count = 0;
            }
            return null;
        });

        client.connect();

        while (true){}
    }
}
