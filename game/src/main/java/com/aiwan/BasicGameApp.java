package com.aiwan;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.client.external.bootstrap.message.ExternalMessage;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import reactor.util.annotation.Nullable;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;


/**
 * 先启动后台进程 GameApp，在启动两个此 BasicGameApp 类
 * @since 2022/12/19
 */
@Slf4j
public class BasicGameApp extends GameApplication {

    public static HashMap<Integer, Function<ExternalMessage, ExternalMessage>> MessagesFunc = new HashMap<>();

    private MyWebSocketClient webSocketClient = null;

    private static Long userId = 0L;

    private static final List<EntityBuilder> builderList = new ArrayList<>();

    private final HashMap<Long,Entity> userMap = new HashMap<>();


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(600);
        settings.setHeight(600);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");

        var wsUrl = "ws://localhost:10100/websocket";
        try {
            webSocketClient = new MyWebSocketClient(wsUrl, (v) -> {
                log.info("打开链接回调！");
                invoke(CmdKit.merge(ActionRouter.SYNC, ActionRouter.V3), (e) -> {
                    byte[] data = e.getData();
                    if (data != null) {
                        Login move = MyWebSocketClient.codec.decode(data, Login.class);
                        log.info("登入: {}", move);
                        userId = move.getUserId();
                        for (Long id : move.getUserIds()) {
                            userMap.putIfAbsent(id, null);
                        }
                        this.sendMove(new Move());
                    }
                    return null;
                });
                return null;
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        webSocketClient.setMessageFunc(byteBuffer -> {
            // 接收服务器返回的消息
            byte[] dataContent = byteBuffer.array();
            ExternalMessage message = DataCodecKit.decode(dataContent, ExternalMessage.class);
            Function<ExternalMessage, ExternalMessage> function = MessagesFunc.get(message.getCmdMerge());
            if (function == null) {
                log.info("未找到路由：收到消息 ExternalMessage ========== \n{}", message);
                return null;
            }
            function.apply(message);
            return null;
        });

        webSocketClient.connect();//连接服务器
    }


    private void invoke(int merge, Function<ExternalMessage, ExternalMessage> func) {
        log.info("发送消息");
        invoke(merge, func, null);
    }

    private void invoke(int merge, Function<ExternalMessage, ExternalMessage> func, @Nullable Object obj) {
        log.info("发送消息1");
        MessagesFunc.putIfAbsent(merge, func);
        ExternalMessage externalMessage = ExternalKit.createExternalMessage(CmdKit.getCmd(merge), CmdKit.getSubCmd(merge), obj);
        byte[] bytes = MyWebSocketClient.codec.encode(externalMessage);
        log.info("发送消息1-5");
        webSocketClient.send(bytes);
        log.info("发送消息2");
    }

    private void sendMove(Move d) {
        Entity entity = userMap.get(userId);
        if (entity != null) {
            log.info("x: {}, y: {}",entity.getX(),entity.getY());
        }

        invoke(CmdKit.merge(ActionRouter.SYNC, ActionRouter.V2), (v) -> {
            byte[] data = v.getData();
            if (data != null) {
                Move move = MyWebSocketClient.codec.decode(data, Move.class);
                log.info("收到移动消息: {}", move);
                this.move(move);
            }
            return null;
        }, d);
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.D, () -> sendMove(Move.D(5)));
        FXGL.onKey(KeyCode.A, () -> sendMove(Move.A(-5)));
        FXGL.onKey(KeyCode.W, () -> sendMove(Move.W(-5)));
        FXGL.onKey(KeyCode.S, () -> sendMove(Move.S(5)));
    }

    public void move(Move move) {
        if (move.getUp() != null) {
            userMap.get(move.getUserId()).translateY(move.getUp());
        }
        if (move.getDown() != null) {
            userMap.get(move.getUserId()).translateY(move.getDown());
        }
        if (move.getLeft() != null) {
            userMap.get(move.getUserId()).translateX(move.getLeft());
        }
        if (move.getRight() != null) {
            userMap.get(move.getUserId()).translateX(move.getRight());
        }

    }
    public static void main(String[] args) {
//        IoGameGlobalSetting.me().setDataCodec(new JsonDataCodec());//设置为Json
//        MyWebSocketClient.codec = new JsonDataCodec();

        launch(args);
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);
        userMap.forEach((e,v)->{
            if (userId != 0 && v == null) {
               var player = FXGL.entityBuilder()
                        .at(300, 300)
                        .view(new Rectangle(25, 25, Color.BLUEVIOLET))
                        .buildAndAttach();
               userMap.put(e,player);
            }
        });
    }
}