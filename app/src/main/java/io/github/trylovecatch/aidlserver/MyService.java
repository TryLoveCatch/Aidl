package io.github.trylovecatch.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by lipeng21 on 2017/8/9.
 */

public class MyService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    class MyBinder extends IMyAidlInterface.Stub
    {

        @Override
        public String getName() throws RemoteException
        {
            return "哈哈哈";
        }
    }
}
