package com.example.jujojazbase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class Notification extends Service {
    public static boolean restart;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        restart = intent.getBooleanExtra("RESTART", true);
        Log.d("Restart", "NotificationService : " + restart);
        return START_STICKY;
    }


    @Override
    public IBinder onBind (Intent intent) {
        Log.println(Log.INFO, "IBINDER(): ", "CALLED!");
        return null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Restart", "NotificationService : " + restart);
        if (restart) {
            Intent broadcastIntent = new Intent(this, BroadcastNotification.class);
            sendBroadcast(broadcastIntent);
        }
    }
}
