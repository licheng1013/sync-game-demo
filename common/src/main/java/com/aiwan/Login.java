package com.aiwan;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lc
 * @since 2022/12/19
 */
@Data
@ProtobufClass
public class Login {
   private Boolean isLogin = true;
   private List<Long> userIds = new ArrayList<>();
   private Long userId;
}
