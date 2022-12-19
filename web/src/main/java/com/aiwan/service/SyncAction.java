package com.aiwan.service;

import cn.hutool.core.util.RandomUtil;
import com.aiwan.ActionRouter;
import com.aiwan.Login;
import com.aiwan.Move;
import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import com.iohao.game.bolt.broker.client.kit.UserIdSettingKit;
import com.iohao.game.bolt.broker.core.client.BrokerClientHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lc
 * @since 2022/12/19
 */

@ActionController(ActionRouter.SYNC)
@Slf4j
public class SyncAction {

    public static Collection<Long> UserIds = new ArrayList<>();


    @ActionMethod(ActionRouter.V3)
    public void login(FlowContext fc){
        int anInt = RandomUtil.randomInt(100, 300);
        boolean success = UserIdSettingKit.settingUserId(fc, anInt);
        Login login = new Login();    // channel 中设置用户的真实 userId；
        if (success) {
            log.info("登入成功");
            UserIds.add((long) anInt);
            login.setUserIds((List<Long>) UserIds);
            login.setUserId((long) anInt);
            // 默认的广播上下文
            var broadcastContext = BrokerClientHelper.me().getBroadcastContext();
            broadcastContext.broadcast(CmdInfo.getCmdInfo(ActionRouter.SYNC, ActionRouter.V3),login,UserIds);
        }

    }


    @ActionMethod(ActionRouter.V2)
    public void test(Move move,FlowContext fc) {
        if (move == null) {
            return;
        }
        move.setUserId(fc.getUserId());
        // 默认的广播上下文
        var broadcastContext = BrokerClientHelper.me().getBroadcastContext();
        broadcastContext.broadcast(CmdInfo.getCmdInfo(ActionRouter.SYNC, ActionRouter.V2),move,UserIds);
    }

}
