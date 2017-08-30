package io.github.trylovecatch.messenger;

import static io.github.trylovecatch.messenger.Constants.WHAT_FROM_CLIENT_CLICK_BTN;
import static io.github.trylovecatch.messenger.Constants.WHAT_FROM_CLIENT_CONNECTED;
import static io.github.trylovecatch.messenger.Constants.WHAT_FROM_SERVICE;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by lipeng21 on 2017/8/14.
 */

public class MessengerService extends Service {


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Messenger tClient = null;
            switch (msg.what){
                case WHAT_FROM_CLIENT_CONNECTED:
                    Log.e("service", "message from client: " + msg.getData().getString("msg"));

                    tClient = msg.replyTo;
                    if(tClient!=null){
                        reply(tClient, "链接成功，哈哈哈");
                    }
                    break;
                case WHAT_FROM_CLIENT_CLICK_BTN:
                    Log.e("service", "message from client: " + msg.getData().getString("msg"));

                    tClient = msg.replyTo;
                    if(tClient!=null){
                        reply(tClient, "我知道你点击了按钮");
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private final Messenger mMessenger = new Messenger(mHandler);


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private void reply(Messenger pClient, String tMsg){
        Message tMessage = Message.obtain(null, WHAT_FROM_SERVICE);
        Bundle tBundle = new Bundle();
        tBundle.putString("reply", tMsg);
        tMessage.setData(tBundle);
        try {
            pClient.send(tMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
