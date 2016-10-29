package leoneo7.biblereading.activity.initial;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leoneo7.biblereading.R;
import leoneo7.biblereading.UserPref;
import leoneo7.biblereading.activity.main.ProgressActivity;
import leoneo7.biblereading.helper.AlarmReceiver;
import leoneo7.biblereading.helper.DBAdapter;

/**
 * Created by ryouken on 2016/09/13.
 */
public class SelectWhenActivity extends Activity {
    @BindView(R.id.when_message)
    TextView mMessage;

    @BindView(R.id.select_start_date)
    DatePicker mDatePicker;

    @BindView(R.id.start_button)
    BootstrapButton mStartButton;

    private DBAdapter dbAdapter = new DBAdapter(this);
    private UserPref mUser= UserPref.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_select_when);
        ButterKnife.bind(this);
    }

    private void startSprint(Calendar calendar) {
        Log.d("startSprint", calendar.getTime().toString());
        long startDate = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, 6);
        long endDate = calendar.getTimeInMillis();
        Log.d("endSprint", calendar.getTime().toString());

        dbAdapter.open();
        dbAdapter.saveSprint(startDate, endDate);
        dbAdapter.close();
        setSprintId();

        calendar.add(Calendar.DATE, -6);
    }

    private void setSprintId() {
        dbAdapter.open();
        Cursor cursor = dbAdapter.getCurrentSprintId();
        startManagingCursor(cursor);
        if (cursor.moveToFirst()) {
            do {
                int sprintId;
                sprintId = cursor.getInt(cursor.getColumnIndex(DBAdapter.SPRINT_ID));
                UserPref.getInstance().setCurrentSprintId(this, sprintId);
            } while (cursor.moveToNext());
        }
        stopManagingCursor(cursor);
        dbAdapter.close();
    }

    private void setupCalendar() {
        int year = mDatePicker.getYear();
        int month = mDatePicker.getMonth();
        int day = mDatePicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        mUser.setStartDate(this, calendar);
        startSprint(calendar);
        AlarmReceiver.alarmManager(this, calendar);
    }

    @OnClick(R.id.start_button)
    public void startButtonClicked() {
        setupCalendar();
        UserPref.getInstance().setIsStarted(this, true);
        Intent intent = new Intent(this, ProgressActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}