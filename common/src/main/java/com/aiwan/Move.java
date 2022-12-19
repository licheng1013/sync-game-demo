package com.aiwan;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @author lc
 * @since 2022/12/19
 */
@Data
@ProtobufClass
public class Move {
    public Move(){};

    private Integer left;
    private Integer right;
    private Integer up;
    private Integer down;

    private Long userId;

    public static Move D(int i) {
        Move move = new Move();
        move.setRight(i);
        return move;
    }


    public static Move A(int i) {
        Move move = new Move();
        move.setLeft(i);
        return move;
    }


    public static Move W(int i) {
        Move move = new Move();
        move.setUp(i);
        return move;
    }


    public static Move S(int i) {
        Move move = new Move();
        move.setDown(i);
        return move;
    }
}
