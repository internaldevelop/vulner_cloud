package com.vulner.bend_server.service;

import com.alibaba.fastjson.JSONObject;

import com.vulner.bend_server.global.websocket.SockMsgTypeEnum;
import com.vulner.bend_server.global.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
@Order(2)
public class AssetCollectScheduler implements CommandLineRunner {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public SystemService systemService = new SystemService();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void beepForAnHour(String infoTypes) {
        // 未指定信息类别时，默认收集CPU使用率和内存使用率
        if (infoTypes == null || infoTypes.isEmpty())
            infoTypes = "Proc CPU Ranking,Proc Memory Ranking,CPU Usage,Mem";

        // 创建一个 Runnable ，设置：任务和项目的 UUID
        MyRunnable runnable = new MyRunnable();
        runnable.setInfoTypes(infoTypes);

        //0延时，每3秒执行下beeper的任务
        final ScheduledFuture<?> beeperHandler = scheduler.scheduleAtFixedRate(runnable, 0, 30, TimeUnit.SECONDS);
//        //执行6秒后，取消beeperHandler任务的执行并退出程序
//        scheduler.schedule(new Runnable() {
//            public void run() {
//                beeperHandler.cancel(true);
//                System.exit(0);
//            }
//        }, 6, TimeUnit.SECONDS);
    }



    private class MyRunnable implements Runnable {
        private String infoTypes;
        @Override
        public void run() {

            // 获取数据
            Object responseObj = systemService.echoAcquire(this.infoTypes);

            // 将节点的资产实时信息通过 websocket 广播到客户端
//            if (responseBean.getCode() == ErrorCodeEnum.ERROR_OK.getCode()) {
            if (true) {
                JSONObject jsonMsg = (JSONObject) JSONObject.toJSON(responseObj);
//                jsonMsg.put("asset_uuid", this.assetUuid);
                WebSocketServer.broadcastAssetInfo(SockMsgTypeEnum.ASSET_REAL_TIME_INFO, jsonMsg);
            }
        }

        public void setInfoTypes(String infoTypes) {
            this.infoTypes = infoTypes;
        }
    }

    @Override
    public void run(String... args) throws Exception {
        new AssetCollectScheduler().beepForAnHour(null);
    }

}
