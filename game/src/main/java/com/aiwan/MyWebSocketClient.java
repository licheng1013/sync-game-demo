package com.aiwan;

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.core.codec.JsonDataCodec;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 * @author lc
 * @since 2022/12/19
 */
@Getter
@Slf4j
public class MyWebSocketClient extends WebSocketClient {
    private final JsonDataCodec codec = new JsonDataCodec();
    private BasicGameApp basicGameApp;

    public MyWebSocketClient(String wsUrl, BasicGameApp basicGameApp) throws URISyntaxException {
        super(new URI(wsUrl), new Draft_6455());
        this.basicGameApp = basicGameApp;
    };

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake open) {
        log.info("打开链接");
        Move move = new Move();
        move.setUp(10);
        ExternalMessage externalMessage = ExternalKit.createExternalMessage(ActionRouter.SYNC, ActionRouter.V1, move);
        byte[] bytes = codec.encode(externalMessage);
        this.send(bytes);
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onMessage(ByteBuffer byteBuffer) {
        // 接收服务器返回的消息
        byte[] dataContent = byteBuffer.array();
        ExternalMessage message = DataCodecKit.decode(dataContent, ExternalMessage.class);
        log.info("收到消息 ExternalMessage ========== \n{}", message);
        byte[] data = message.getData();
        if (data != null) {
            Move move = codec.decode(data, Move.class);
            log.info("helloReq ========== \n{}", move);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }
}
