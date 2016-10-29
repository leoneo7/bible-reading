package leoneo7.biblereading.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;

import leoneo7.biblereading.UserPref;

/**
 * Created by ryouken on 2016/10/01.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ||
                Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            Log.d("BootCompleted", "---------------------");
            long startDate = UserPref.getInstance().getStartDate(context);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startDate);
            alarmManager(context, calendar);
        } else {
            Log.d("onReceive", "---------------------");
            Calendar calendar = setCalendar();
            startSprint(context, calendar);
            UserPref.getInstance().setDoneChapters(context, 0);
        }
    }

    private void startSprint(Context context, Calendar calendar) {
        Log.d("startSprint", calendar.getTime().toString());
        long startDate = calendar.getTimeInMillis();

        calendar.add(Calendar.DATE, 6);
        long endDate = calendar.getTimeInMillis();
        Log.d("startSprint", calendar.getTime().toString());

        DBAdapter dbAdapter = new DBAdapter(context);
        dbAdapter.open();
        dbAdapter.saveSprint(startDate, endDate);
        dbAdapter.close();
    }

    @NonNull
    private Calendar setCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        calendar.set(year, month, date, 0, 0, 0);
        Log.d("AlarmSetCalendar", calendar.getTime().toString());
        return calendar;
    }

    public static void alarmManager(Context context, Calendar calendar) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);
        calendar.add(Calendar.DATE, 7);
        long setTime = calendar.getTimeInMillis();
        Log.d("setTime", String.valueOf(calendar.getTime()));
        long interval = AlarmManager.INTERVAL_DAY * 7;
        calendar.setTimeInMillis(setTime + interval);
        Log.d("interval", String.valueOf(calendar.getTime()));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setTime, interval, pending);
    }

}