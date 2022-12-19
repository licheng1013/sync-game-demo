package com.aiwan;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;

import java.net.URISyntaxException;

@Slf4j
public class BasicGameApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(600);
        settings.setHeight(600);
        settings.setTitle("Basic Game App");
        settings.setVersion("0.1");

        var wsUrl = "ws://localhost:10100/websocket";
        WebSocketClient webSocketClient = null;
        try {
            webSocketClient = new MyWebSocketClient(wsUrl,this);
            webSocketClient.connect();//连接服务器
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.D, () -> {
            player.translateX(5); // move right 5 pixels
        });

        FXGL.onKey(KeyCode.A, () -> {
            player.translateX(-5); // move left 5 pixels
        });

        FXGL.onKey(KeyCode.W, () -> {
            player.translateY(-5); // move up 5 pixels
        });

        FXGL.onKey(KeyCode.S, () -> {
            player.translateY(5); // move down 5 pixels
        });
    }


    public void move(Move move){
        if (move.getUp() != null) {
            player.translateY(move.getUp());
        }
        if (move.getDown() != null){
            player.translateY(move.getDown());
        }
        if (move.getLeft() != null) {
            player.translateY(move.getLeft());
        }
        if (move.getRight() != null) {
            player.translateX(move.getRight());
        }
    }


    public static void main(String[] args)  {
        launch(args);
    }

    private Entity player;

    @Override
    protected void initGame() {
        player = FXGL.entityBuilder()
                .at(300, 300)
                .view(new Rectangle(25, 25, Color.BLUEVIOLET))
                .buildAndAttach();
    }

    @Override
    protected void initUI() {
        //这里是添加面板
//        Text textPixels = new Text();
//        textPixels.setText("Ok");
//        textPixels.setTranslateX(50); // x = 50
//        textPixels.setTranslateY(100); // y = 100
//        getGameScene().addUINode(textPixels); // add to the scene graph
    }

}