package com.example.jisansheikh.rudealarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jisansheikh.rudealarm.R;

import java.util.Calendar;

public class MainActivity extends Activity {
    AlarmManager alarmManager;
    TimePicker alarmTime;
    Context context;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        this.context = this;
        alarmTime = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Calendar calendar = Calendar.getInstance();
        final Intent intent = new Intent(this.context, alarmReciever.class);

        Button set = (Button) findViewById(R.id.setAlarm);
        Button end = (Button) findViewById(R.id.endAlarm);

        set.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int hour = alarmTime.getHour();
                int minute = alarmTime.getMinute();
                String hours = String.valueOf(hour);
                String minutes = String.valueOf(minute);
                String div = "AM.";
                if(hour > 12){
                    hours = String.valueOf(hour - 12);
                    div = "PM.";
                }
                if(minute < 10){
                    minutes = "0" + minutes;
                }

                Toast.makeText(MainActivity.this, "Alarm is set to " + hours + ":" + minutes + " " + div
                        , Toast.LENGTH_SHORT).show();
                intent.putExtra("extra", "yes");
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                calendar.set(Calendar.HOUR_OF_DAY, alarmTime.getHour());
                calendar.set(Calendar.MINUTE, alarmTime.getMinute());
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 5000, pendingIntent);



            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("extra", "no");
                alarmManager.cancel(pendingIntent);
                sendBroadcast(intent);

            }
        });
    }
}
