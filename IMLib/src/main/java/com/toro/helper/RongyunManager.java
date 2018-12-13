package com.toro.helper;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Create By liujia
 * on 2018/10/30.
 **/
public class RongyunManager {

    private static RongyunManager instance;
    private Context appContext;
    private List<RongYunListener.OnReceiveMessageListener> onReceiveMessageListeners;
    private List<RongYunListener.ConnectionStatusListener> connectionStatusListeners;
    private boolean isFirst = true;

    public static RongyunManager getInstance() {
        if(instance == null) {
            instance = new RongyunManager();
        }
        return instance;
    }

    private RongyunManager() {
        onReceiveMessageListeners = new ArrayList<RongYunListener.OnReceiveMessageListener>();
        connectionStatusListeners = new ArrayList<RongYunListener.ConnectionStatusListener>();
    }

    public void init(Context context) {
        this.appContext = context;
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (context.getPackageName().equals(getCurProcessName(context)) || "io.rong.push".equals(getCurProcessName(context))) {

            /**
             * IM SDK调用第一步 初始化
             */
            RongIMClient.init(context);
        }
        RongIMClient.setOnReceiveMessageListener(onReceiveMessageListener);
        RongIMClient.setConnectionStatusListener(connectionStatusListener);
    }

    public void disConnect() {
        RongIMClient.getInstance().disconnect();
    }

    public void connect(String token,final RongYunListener.RongYunConnectCallback callback) {
//        if(!isFirst) {
//            return;
//        }
//        isFirst = false;
//        RongIMClient.getInstance().disconnect();
        Log.e("RongyunManager","token===>"+token);
        RongIMClient.getInstance().connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                if(callback != null) {
                    callback.onTokenIncorrect();
                }
            }

            @Override
            public void onSuccess(String userid) {
                if(callback != null) {
                    callback.onSuccess(userid);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                if(callback != null) {
                    callback.onError(errorCode.getValue());
                }
            }
        });
    }

    public String getCurrentConnectionStatus(){
        return RongIMClient.getInstance().getCurrentConnectionStatus().getMessage();
    }

    public void addOnReceiveMessageListener(RongYunListener.OnReceiveMessageListener listener){
        if(listener instanceof RongYunListener.OnReceiveMessageListener) {
            onReceiveMessageListeners.add(listener);
        }
    }

    public void removeOnReceiveMessageListener(RongYunListener.OnReceiveMessageListener listener){
        if(listener instanceof RongYunListener.OnReceiveMessageListener) {
            onReceiveMessageListeners.remove(listener);
        }
    }

    public void addConnectionStatusListener(RongYunListener.ConnectionStatusListener listener) {
        connectionStatusListeners.add(listener);
    }

    public void removeConnectionStatusListener(RongYunListener.ConnectionStatusListener listener) {
        connectionStatusListeners.remove(listener);
    }

    private OnReceiveMessageListener onReceiveMessageListener = new OnReceiveMessageListener() {
        @Override
        public boolean onReceived(Message message, int i) {
            Log.e("***","=========>收到消息"+message.getContent());
            String result = "";
            try{
                if (message.getContent() instanceof TextMessage){
                    TextMessage tm = (TextMessage) message.getContent();
                    JSONObject obj = new JSONObject(tm.getContent());
                    result = obj.getString("content");
                }
            }catch (Exception e) {
                return false;
            }
            boolean flag = false;
            if(onReceiveMessageListeners != null){
                for(RongYunListener.OnReceiveMessageListener listener : onReceiveMessageListeners) {
                    if(listener.onReceived(result)){
                        flag = true;
                    }
                }
            }
            return flag;
        }
    };

    private RongIMClient.ConnectionStatusListener connectionStatusListener = new RongIMClient.ConnectionStatusListener() {
        @Override
        public void onChanged(ConnectionStatus connectionStatus) {
            if(connectionStatusListeners != null){
                for(RongYunListener.ConnectionStatusListener listener : connectionStatusListeners) {
                    listener.onChanged(connectionStatus.name());
                }
            }
        }
    };

    public void pushKicked(){
        if(connectionStatusListeners != null){
            for(RongYunListener.ConnectionStatusListener listener : connectionStatusListeners) {
                listener.onChanged(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT.name());
            }
        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager)context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
