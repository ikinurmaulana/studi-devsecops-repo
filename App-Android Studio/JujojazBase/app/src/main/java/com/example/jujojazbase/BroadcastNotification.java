package com.example.jujojazbase;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import json.JSONArray;
import json.JSONObject;
import kotlin.Unit;

import static android.content.Context.NOTIFICATION_SERVICE;

public class BroadcastNotification extends BroadcastReceiver {
    private Intent intent;
    private Context context;

    public void showNotification(String title, String msg){
        final int NOTIFICATION_ID = 1;
        final String NOTIFICATION_CHANNEL_ID = "jujojaz_app";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Jujojaz-APP");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.jujojaz_logo)
                .setContentTitle(title)
                .setContentText(msg);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private Unit onDone(JSONObject json){
        Log.println(Log.INFO, "onDone Broadcast(): ", "we are in ondone");
        JSONArray all_pajak = (JSONArray) json.get("pajak");
        JSONArray all_servis = (JSONArray) json.get("servis");

        String pajak_title = "Wah harus bayar pajak nih";
        String servis_title = "Ada yang mau jajan nih";
        String pajak_msg = "Pajak pada mobil %s anda harus dibayarkan %d hari lagi";
        String servis_msg = "Anda dianjurkan servis mobil %s pada %d hari kedepan.";


        for (Object cd : all_pajak){
            JSONArray car_days = (JSONArray) cd;
            String msg = "Pajak mobil " + car_days.get(0) + " anda harus dibayarkan " + car_days.get(1) + " hari lagi";
            showNotification(pajak_title, msg);
        }

        for (Object cd : all_servis){
            JSONArray car_days = (JSONArray) cd;
            String msg = "Anda dianjurkan untuk servis mobil " + car_days.get(0) + " anda " + car_days.get(1) + " hari lagi";
            showNotification(servis_title, msg);
        }
        
        return Unit.INSTANCE;
    }

    private Unit onError(String msg){
        return Unit.INSTANCE;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.intent = intent;
        this.context = context;
        Log.println(Log.INFO, "onReceive(): ", "will sending...");
        JujojazLib.Companion.hitAPI("/api/checkpajakservis/", Auth.authJson, null, this::onDone, this::onError, false);
        Log.println(Log.INFO, "onReceive(): ", "sended");
    }

    public boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Notification.class.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
