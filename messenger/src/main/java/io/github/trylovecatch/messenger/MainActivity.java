package io.github.trylovecatch.messenger;

import static io.github.trylovecatch.messenger.Constants.WHAT_FROM_CLIENT_CLICK_BTN;
import static io.github.trylovecatch.messenger.Constants.WHAT_FROM_CLIENT_CONNECTED;
import static io.github.trylovecatch.messenger.Constants.WHAT_FROM_SERVICE;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case WHAT_FROM_SERVICE:
                    Log.e("client", "receive msg from service: " + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };
    private final Messenger mMessengerClient = new Messenger(mHandler);

    private Messenger mMessengerService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessengerService = new Messenger(service);
            send(WHAT_FROM_CLIENT_CONNECTED, "hello, hahahaha");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent tIntent = new Intent(this, MessengerService.class);
        bindService(tIntent, mServiceConnection, BIND_AUTO_CREATE);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(WHAT_FROM_CLIENT_CLICK_BTN, "click client btn");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    private void send(int pWhat, String tMsg){
        if(mMessengerService !=null){
            Message tMessage = Message.obtain(null, pWhat);
            Bundle tData = new Bundle();
            tData.putString("msg", tMsg);
            tMessage.setData(tData);
            tMessage.replyTo = mMessengerClient;
            try {
                mMessengerService.send(tMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
