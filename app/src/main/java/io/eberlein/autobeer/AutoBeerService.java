package io.eberlein.autobeer;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.Nullable;

public class AutoBeerService extends NotificationListenerService {
    private static final String TAG = "AutoBeerService";
    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "-----------------------------");
            Log.d(TAG, intent.toString());
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG, "onNotificationPosted");
        Log.d(TAG, sbn.getPackageName());
        Log.d(TAG, sbn.getTag());
        try {
            sbn.getNotification().actions[0].actionIntent.send();
            Log.d(TAG, "PROSTED");
        } catch (PendingIntent.CanceledException e){
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        registerReceiver(serviceReceiver, new IntentFilter());
        Log.d(TAG, "onCreate done");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        unregisterReceiver(serviceReceiver);
        Log.d(TAG, "onDestroy done");
    }
}
