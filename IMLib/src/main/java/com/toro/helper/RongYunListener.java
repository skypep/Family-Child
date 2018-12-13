package com.toro.helper;

/**
 * Create By liujia
 * on 2018/10/31.
 **/
public class RongYunListener {

    public interface RongYunConnectCallback {
        /**
         * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
         *                            2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
         */
        public void onTokenIncorrect();

        /**
         * 连接融云成功
         * @param userid 当前 token 对应的用户 id
         */
        public void onSuccess(String userid);

        /**
         * 连接融云失败
         * @param errorCode 错误码，可到官网 查看错误码对应的注释
         */
        public void onError(int errorCode);
    }

    public interface OnReceiveMessageListener{
        public boolean onReceived(String message);
    }

    public interface ConnectionStatusListener{
        public void onChanged(String name);
    }

}
