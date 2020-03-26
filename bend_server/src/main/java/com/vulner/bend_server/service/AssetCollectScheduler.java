package com.vulner.bend_server.service;

import com.alibaba.fastjson.JSONObject;
import com.vulner.bend_server.global.websocket.SockMsgTypeEnum;
import com.vulner.bend_server.global.websocket.WebSocketServer;
import com.vulner.common.utils.StringUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class AssetCollectScheduler {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private SystemService systemService = new SystemService();

    private ScheduledExecutorService threadPoolTaskScheduler = Executors.newScheduledThreadPool(1);

    public static List<TimerTask> timerTaskList = new ArrayList<>();

    @Data
    private class TimerTask {
        private ScheduledFuture<?> future;
        private String assetUuid;
    }

    /**
     * 启动任务
     * @param assetUuid
     * @param assetIp
     * @param infoTypes
     * @param secondTime 每secondTime秒执行一次
     * @return
     */
    public boolean startTask(String assetUuid, String assetIp, String infoTypes, long secondTime) {
        // 未指定信息类别时，默认收集CPU使用率和内存使用率
        if (!StringUtils.isValid(infoTypes))
            infoTypes = "Proc CPU Ranking,Proc Memory Ranking,CPU Usage,Mem";
        if (secondTime < 3)
            secondTime = 3;

        rmList(timerTaskList, assetUuid);  //先停止assetUuid任务

        // 创建一个 Runnable ，设置：任务和项目的 UUID
        MyRunnable runnable = new MyRunnable();
        runnable.setAssetUuid(assetUuid);
        runnable.setAssetIp(assetIp);
        runnable.setInfoTypes(infoTypes);

        //0延时，每secondTime秒执行下beeper的任务
        ScheduledFuture<?> future = threadPoolTaskScheduler.scheduleAtFixedRate(runnable, 0, secondTime, TimeUnit.SECONDS);

        if (future == null)
            return false;

        TimerTask timerTask = new TimerTask();
        timerTask.setAssetUuid(assetUuid);
        timerTask.setFuture(future);
        timerTaskList.add(timerTask);

        return true;
    }

    // 停止任务
    public boolean stopTask(String assetUuid) {
        if (!StringUtils.isValid(assetUuid))
            return false;

        return rmList(timerTaskList, assetUuid);

//        for (TimerTask timerTask : this.timerTaskList) {
//            // 在任务计划列表中查找
//            if (timerTask.getAssetUuid().equals(assetUuid)) {
//                // 如果任务和资产的 UUID 匹配，则取消该任务计划
//                ScheduledFuture<?> future = timerTask.getFuture();
//                if (future != null)
//                    future.cancel(true);
//                // 移除任务计划
//                timerTaskList.remove(timerTask);
//                return true;
//            }
//        }
    }

    // 循环删除list元素
    public boolean rmList (List<TimerTask> tList, String assetUuid) {
        Iterator<TimerTask> it = tList.iterator();

        boolean flag = false;
        while(it.hasNext()){
            TimerTask tTask = it.next();

            if (assetUuid.equals(tTask.assetUuid)) {
                // 如果任务和资产的 UUID 匹配，则取消该任务计划
                ScheduledFuture<?> future = tTask.future;
                if (future != null)
                    future.cancel(true);
                // 移除任务计划
                it.remove();
                flag = true;
            }

        }
        return flag;
    }

    @Data
    private class MyRunnable implements Runnable {
        private String assetUuid ;
        private String assetIp;
        private String infoTypes;

        @Override
        public void run() {

            Object responseObj = null;
            // 获取数据
            if (StringUtils.isValid(assetIp)) { // 调用节点

            } else { // 调用本地
                responseObj = systemService.echoAcquire(this.infoTypes);
            }

            // 将节点的资产实时信息通过 websocket 广播到客户端
//            if (responseBean.getCode() == ErrorCodeEnum.ERROR_OK.getCode()) {
            if (true) {
                JSONObject jsonMsg = (JSONObject) JSONObject.toJSON(responseObj);
//                jsonMsg.put("asset_uuid", this.assetUuid);
                WebSocketServer.broadcastAssetInfo(SockMsgTypeEnum.ASSET_REAL_TIME_INFO, jsonMsg);
            }
        }

    }

}
