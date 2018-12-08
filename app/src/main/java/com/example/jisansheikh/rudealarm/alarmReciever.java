package com.example.jisansheikh.rudealarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class alarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("yes");
        Log.e("We are in the Reciever", "yay");
        String alarmStatus = intent.getExtras().getString("extra");
        Intent serviceIntent = new Intent(context, ringTonePlayer.class);
        serviceIntent.putExtra("extra", alarmStatus);
        PowerManager mgr = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();
        context.startService(serviceIntent);


    }
}
