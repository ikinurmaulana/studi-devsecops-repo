package com.example.jujojazbase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmManagerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent broadcastNotification = new Intent(context, BroadcastNotification.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, broadcastNotification, PendingIntent.FLAG_CANCEL_CURRENT);
        assert alarmManager != null;
        boolean startAlarm = intent.getBooleanExtra("STARTALARM", true);
        if (startAlarm) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 2000, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
        boolean isWorking = (PendingIntent.getBroadcast(context, 0, broadcastNotification, PendingIntent.FLAG_NO_CREATE) != null);
        Log.d("startAlarm", String.valueOf(startAlarm));
        Log.d("checkAlarm", String.valueOf(isWorking));
    }
}
