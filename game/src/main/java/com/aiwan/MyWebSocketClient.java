package com.aiwan;

import com.iohao.game.action.skeleton.core.codec.DataCodec;
import com.iohao.game.action.skeleton.core.codec.ProtoDataCodec;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * @author lc
 * @since 2022/12/19
 */
@Getter
@Setter
@Slf4j
public class MyWebSocketClient extends WebSocketClient {
    public static final DataCodec codec = new ProtoDataCodec();
    private Function<MyWebSocketClient, MyWebSocketClient> func;
    private Function<ByteBuffer, Boolean> messageFunc;


    public MyWebSocketClient(String wsUrl, Function<MyWebSocketClient,MyWebSocketClient> func) throws URISyntaxException {
        super(new URI(wsUrl), new Draft_6455());
        this.func = func;
    };

    @Override
    public void onOpen(ServerHandshake open) {
        this.func.apply(this); //TODO 初始化回调
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onMessage(ByteBuffer byteBuffer) {
        this.messageFunc.apply(byteBuffer);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }

    @Override
    public void connect() {
        if (this.messageFunc == null) {
            throw new RuntimeException("请注册消息接受器！");
        }
        super.connect();
    }
}
