package leoneo7.biblereading.activity.initial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by ryouken on 2016/09/13.
 */
public class SelectWhenActivity extends Activity {
    @BindView(R.id.when_message)
    TextView mMessage;

    @BindView(R.id.select_start_date)
    DatePicker mDatePicker;

    @BindView(R.id.tmp_button)
    BootstrapButton mTmpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_select_when);
        ButterKnife.bind(this);

        setupCalendar();
    }

    private void setupCalendar() {
        int year = mDatePicker.getYear();
        int month = mDatePicker.getMonth();
        int day = mDatePicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        UserPref.getInstance().setStartDate(this, calendar);
    }

    @OnClick(R.id.tmp_button)
    public void tmpButtonClicked() {
        setupCalendar();
        Intent intent = new Intent(this, ProgressActivity.class);
        startActivity(intent);
    }

}