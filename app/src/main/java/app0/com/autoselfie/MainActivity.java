package app0.com.autoselfie;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String storagePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int storageResponsePermission = getApplicationContext().checkCallingOrSelfPermission(storagePermission);


        String cameraPermission = Manifest.permission.CAMERA;
        int cameraResponsePermission = getApplicationContext().checkCallingOrSelfPermission(cameraPermission);


        if (storageResponsePermission != PackageManager.PERMISSION_GRANTED || cameraResponsePermission != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,


            }, 0);

        }

        setContentView(R.layout.activity_main);


        /** Set Alarm for resetting students online status at 12 midnight everyday*/
//        int alarmId = (int) Math.random();
//        Calendar calendar = Calendar.getInstance();
//        setDailyAlarmOn(getApplicationContext(), /* calendar.getTimeInMillis() */ getMillisecondsTillNextMidnight(), Uri.parse(String.valueOf(alarmId)));
    }


    public void onGoToSignupActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    public void onGoToScheduleArrangementActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), ScheduleArrangementActivity.class);
        startActivity(intent);
    }

    public void setDailyAlarmOn(Context context, long alarmTime, Uri reminderTask) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        PendingIntent operation =
                StudentStatusService.getReminderPendingIntent(context, reminderTask);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, operation);

    }

    public static long getMillisecondsTillNextMidnight() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1); //add a day first
        c.set(Calendar.HOUR_OF_DAY, 0); //then set the other fields to 0
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis() - System.currentTimeMillis();
    }


}