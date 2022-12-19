package com.aiwan.service;

import com.aiwan.ActionRouter;
import com.aiwan.Move;
import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.protocol.RequestMessage;
import com.iohao.game.bolt.broker.client.external.bootstrap.ExternalKit;
import com.iohao.game.bolt.broker.core.client.BrokerClientHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lc
 * @since 2022/12/19
 */

@ActionController(ActionRouter.SYNC)
@Slf4j
public class SyncAction {
    @ActionMethod(ActionRouter.V1)
    public void sync(Move move) {
        log.info("收到消息: ${}",move);
        RequestMessage message = ExternalKit.createRequestMessage(CmdKit.merge(ActionRouter.SYNC, ActionRouter.V1), move);
        // 默认的广播上下文
        var broadcastContext = BrokerClientHelper.me().getBroadcastContext();
        broadcastContext.broadcast(message.createResponseMessage());
    }
}
