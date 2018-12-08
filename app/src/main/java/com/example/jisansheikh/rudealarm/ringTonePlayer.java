package com.example.jisansheikh.rudealarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.security.Provider;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.*;

public class ringTonePlayer extends Service{
    MediaPlayer alarm;
    int startID;
    public boolean isRunning;
    int shake = 0;
    SensorManager sm;
    float preaccl;
    float accl;
    float dis;

    @Override
    public void onCreate() {
        super.onCreate();
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        preaccl = SensorManager.GRAVITY_EARTH;
        accl = SensorManager.GRAVITY_EARTH;
        dis = 0.0f;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startID){


        String state = intent.getExtras().getString("extra");


        Log.e("In", " Ring. "+ this.isRunning + " " + startID + " " + state);
        if(!this.isRunning && state.equals("yes")){
            alarm = MediaPlayer.create(this, R.raw.alarm);
            alarm.setLooping(true);
            alarm.start();
            this.isRunning = true;
            this.startID = 0;
            Log.e("Start"," Alarm");
        }
        else if(shake >= 500){
            Log.e("Stop", ""+shake);

            alarm.stop();
            alarm.reset();
            this.isRunning = false;
            this.startID = 0;
            shake = 0;
        }
        else if(!this.isRunning && state.equals("no")){
            this.isRunning = false;
            this.startID = 0;
            shake = 0;
        }
        else if(this.isRunning && state.equals("yes")){
            this.isRunning = true;
            this.startID = 1;
        }


        return START_NOT_STICKY;
    };
    @Override
    public void onDestroy(){
        super.onDestroy();
        this.isRunning = false;
    }
    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            preaccl = accl;
            accl = (float) Math.sqrt(x*x + y*y + z*z);
            float del = Math.abs(accl - preaccl);
            dis = dis * .9f + del;
            if(dis > 12 && isRunning) {
                shake++;
                if(shake >= 500) {
                    makeText(getApplicationContext(),getString(R.string.msg), LENGTH_LONG).show();
                }
                Log.e("Shaking", "" + shake);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
