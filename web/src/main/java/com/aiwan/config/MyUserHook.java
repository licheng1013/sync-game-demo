package com.aiwan.config;

import com.aiwan.service.SyncAction;
import com.iohao.game.bolt.broker.client.external.session.UserSession;
import com.iohao.game.bolt.broker.client.external.session.UserSessions;
import com.iohao.game.bolt.broker.client.external.session.hook.UserHook;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyUserHook implements UserHook {
    public void into(UserSession userSession) {
        long userId = userSession.getUserId();
        log.info("玩家上线 userId: {} -- {}", userId, userSession.getUserChannelId());
        log.info("当前在线玩家数量： {}", UserSessions.me().countOnline());
    }

    public void quit(UserSession userSession) {
        long userId = userSession.getUserId();
        log.info("玩家退出 userId: {} -- {}", userId, userSession.getUserChannelId());
        log.info("当前在线玩家数量： {}", UserSessions.me().countOnline());
        SyncAction.UserIds.remove(userId);
    }
}