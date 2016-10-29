package leoneo7.biblereading.activity.main;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leoneo7.biblereading.R;
import leoneo7.biblereading.ReadLog;
import leoneo7.biblereading.UserPref;
import leoneo7.biblereading.activity.menu.AboutAppActivity;
import leoneo7.biblereading.activity.menu.SettingActivity;
import leoneo7.biblereading.helper.DBAdapter;

/**
 * Created by ryouken on 2016/09/13.
 */
public class ProgressActivity extends AppCompatActivity {

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    @BindView(R.id.pie_chart)
    PieChart mPieChart;

    @BindView(R.id.minus_button)
    ImageButton mMinusButton;

    @BindView(R.id.plus_button)
    ImageButton mPlusButton;

    @BindView(R.id.read_chapter)
    TextView mReadText;

    @BindView(R.id.achieve_button)
    BootstrapButton mAchieveButton;

    @BindView(R.id.daily_read)
    TextView mDailyRead;

    @BindView(R.id.exp_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.level_text)
    TextView mLevelText;

    private DBAdapter dbAdapter = new DBAdapter(this);
    private UserPref mUser = UserPref.getInstance();
    private ArrayList<ReadLog> mReadLogList = new ArrayList<>();
    private int mCurrentSprintId;
    private int mReadNumber = 0;
    private int mDoneChapters = 0;
    private int duplicatedLogId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_progress);

        ButterKnife.bind(this);

        onClickMenu();
        setSprintId();
        setReadLogList(mCurrentSprintId, mReadLogList);
        setupToolBar();
        setupPieChartView();
        setupText();
        setupProgressBar();
        setupLevel();
    }

    private void setupToolBar() {
        setSupportActionBar(mToolBar);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.xbox_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mToolBar.setBackgroundColor(Color.rgb(70, 70, 70));
        mToolBar.setTitle("MENU");
        mToolBar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupPieChartView() {
        calculateDoneChapters();
        int weekTargetChapters = mUser.getTargetChapter(this) * 7;
        int leftChapters = weekTargetChapters - mDoneChapters;

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(mDoneChapters, 0));
        entries.add(new PieEntry(leftChapters, 1));

        PieDataSet dataSet = new PieDataSet(entries, "");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(this, R.color.twitter));
        colors.add(Color.argb(100, 180, 180, 180));
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(false);

        if (leftChapters < 0) {
            mPieChart.setCenterText(generateCenterSpannableText(true, leftChapters));
        } else {
            mPieChart.setCenterText(generateCenterSpannableText(false, leftChapters));
        }
        mPieChart.setDescription("");
        mPieChart.setRotationEnabled(false);
        mPieChart.getLegend().setEnabled(false);
        mPieChart.setHoleRadius(60f);
        mPieChart.animateY(2000, Easing.EasingOption.EaseInQuart);
        mPieChart.setData(pieData);
    }

    private SpannableString generateCenterSpannableText(boolean isOver, int leftChapters) {
        if (isOver) {
            leftChapters = Math.abs(leftChapters);
            SpannableString s = new SpannableString("貯金\n" + ((Integer) leftChapters).toString() + "章");
            s.setSpan(new RelativeSizeSpan(4.5f), 3, 5, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 3, 5, 0);
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.twitter)), 3, 5, 0);
            return s;
        } else {
            SpannableString s = new SpannableString("残り\n" + ((Integer) leftChapters).toString() + "章");
            s.setSpan(new RelativeSizeSpan(4.5f), 3, 5, 0);
            s.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 3, 5, 0);
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.twitter)), 3, 5, 0);
            return s;
        }
    }

    private void setupText() {
        mReadText.setText(String.valueOf(mReadNumber));
        if (mReadNumber >= mUser.getTargetChapter(this)) {
            mReadText.setTextColor(Color.rgb(0, 150, 0));
            mAchieveButton.setText(R.string.achieve_button);
        } else {
            mReadText.setTextColor(Color.RED);
            mAchieveButton.setText(R.string.save_button);
        }
    }

    private void setupLevel() {
        mLevelText.setText(String.valueOf(mUser.getLevel(this)));
    }

    private void setupProgressBar() {
        mProgressBar.setMax(mUser.getLevel(this) * 500);
        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", mUser.getEXP(this));
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @OnClick(R.id.minus_button)
    public void onMinusButton() {
        if (mReadNumber == 0) return;
        mReadNumber --;
        setupText();
    }

    @OnClick(R.id.plus_button)
    public void onPlusButton() {
        mReadNumber ++;
        setupText();
    }

    @OnClick(R.id.achieve_button)
    public void onAchieveButton() {
        if (mReadNumber == 0) return;

        Calendar calendar = setCalendar();
        if (checkDuplicated(calendar)) {
            confirmUpdate(calendar, mCurrentSprintId);
            return;
        } else {
            saveLog(calendar, mCurrentSprintId);
        }

        int point = mUser.setRandomEXP(mReadNumber);
        mUser.addEXP(this, point);
        mUser.setDoneChapters(this, mUser.getDoneChapters(this) + mReadNumber);

        setReadLogList(mCurrentSprintId, mReadLogList);
        setupPieChartView();
        setupProgressBar();
        setupLevel();
        String message = "+" + ((Integer) point).toString() + "EXP";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void confirmUpdate(final Calendar calendar, final int sprintId) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.already_saved)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        updateLog(calendar, sprintId);
                        setReadLogList(mCurrentSprintId, mReadLogList);
                        setupPieChartView();
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        return;
                    }
                })
                .show();
    }

    private void saveLog(Calendar calendar, int currentSprintId){
        dbAdapter.open();
        dbAdapter.saveLog(calendar.getTimeInMillis(), mReadNumber, currentSprintId);
        dbAdapter.close();
    }

    private void updateLog(Calendar calendar, int currentSprintId) {
        dbAdapter.open();
        dbAdapter.updateLog(duplicatedLogId, calendar.getTimeInMillis(), mReadNumber, currentSprintId);
        dbAdapter.close();
    }

    @NonNull
    private Calendar setCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        calendar.set(year, month, date, 0, 0, 0);
        Log.d("saveLog", calendar.getTime().toString());
        return calendar;
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
                mCurrentSprintId = mUser.getCurrentSprintId(this);
            } while (cursor.moveToNext());
        }
        stopManagingCursor(cursor);
        dbAdapter.close();
    }

    private boolean checkDuplicated(Calendar calendar) {
        long setDate = calendar.getTimeInMillis();
        for (ReadLog readLog : mReadLogList) {
            long date = readLog.getDate().getTimeInMillis();
            if (Math.abs(date - setDate) < 1000) {
                duplicatedLogId = readLog.getId();
                return true;
            }
        }
        return false;
    }

    private void setReadLogList(int sprintId, ArrayList<ReadLog> readLogList) {
        readLogList.clear();
        dbAdapter.open();
        Cursor cursor = dbAdapter.getReadLogInCurrentSprint(sprintId);
        startManagingCursor(cursor);
        if (cursor.moveToFirst()) {
            do {
                ReadLog readLog = new ReadLog(
                        cursor.getInt(cursor.getColumnIndex(DBAdapter.LOG_ID)),
                        cursor.getLong(cursor.getColumnIndex(DBAdapter.LOG_DATE)),
                        cursor.getInt(cursor.getColumnIndex(DBAdapter.LOG_CHAPTERS)),
                        cursor.getInt(cursor.getColumnIndex(DBAdapter.SPRINT_ID)));
                readLogList.add(readLog);
            } while (cursor.moveToNext());
        }
        stopManagingCursor(cursor);
        dbAdapter.close();
    }

    private void calculateDoneChapters() {
        mDoneChapters = 0;
        for (ReadLog readLog : mReadLogList) {
            mDoneChapters += readLog.getChapters();
        }
    }

    private void onClickMenu() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        Intent intent_progress = new Intent(ProgressActivity.this, ProgressActivity.class);
                        startActivity(intent_progress);
                        break;
//                    case R.id.menu_profile:
//                        Intent intent_profile = new Intent(ProgressActivity.this, ProfileActivity.class);
//                        startActivity(intent_profile);
//                        break;
                    case R.id.menu_history:
                        Intent intent_history = new Intent(ProgressActivity.this, HistoryActivity.class);
                        startActivity(intent_history);
                        break;
//                    case R.id.menu_ranking:
//                        break;
//                    case R.id.menu_notification:
//                        Intent intent_notification = new Intent(ProgressActivity.this, NotificationActivity.class);
//                        startActivity(intent_notification);
//                        break;
                    case R.id.menu_about:
                        Intent intent_about = new Intent(ProgressActivity.this, AboutAppActivity.class);
                        startActivity(intent_about);
                        break;
//                    case R.id.menu_help:
//                        Intent intent_help = new Intent(ProgressActivity.this, HelpActivity.class);
//                        startActivity(intent_help);
//                        break;
                    case R.id.menu_setting:
                        Intent intent_setting = new Intent(ProgressActivity.this, SettingActivity.class);
                        startActivity(intent_setting);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

}
