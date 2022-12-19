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
    private Integer left;
    private Integer right;
    private Integer up;
    private Integer down;
}
