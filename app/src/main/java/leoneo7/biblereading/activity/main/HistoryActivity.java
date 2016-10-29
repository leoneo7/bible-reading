package leoneo7.biblereading.activity.main;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leoneo7.biblereading.R;
import leoneo7.biblereading.ReadLog;
import leoneo7.biblereading.SprintLog;
import leoneo7.biblereading.UserPref;
import leoneo7.biblereading.activity.menu.AboutAppActivity;
import leoneo7.biblereading.activity.menu.SettingActivity;
import leoneo7.biblereading.helper.DBAdapter;

/**
 * Created by ryouken on 2016/09/13.
 */
public class HistoryActivity extends AppCompatActivity {
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    @BindView(R.id.log_list)
    ExpandableListView mListView;

    List<Map<String, String>> parentList = new ArrayList<>();
    List<List<Map<String, String>>> allChildList = new ArrayList<>();

    private ArrayList<SprintLog> mSprintLogList = new ArrayList<>();
    private ArrayList<ReadLog> mReadLogList = new ArrayList<>();
    private DBAdapter dbAdapter = new DBAdapter(this);
    private UserPref mUser = UserPref.getInstance();
    private int mSprintId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_history);
        ButterKnife.bind(this);

        onClickMenu();
        setupToolBar();

        setupData();
        setupExpandableAdapter();
    }

    private void setupExpandableAdapter() {
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this, parentList,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{"title"}, new int[]{android.R.id.text1},
                allChildList, android.R.layout.simple_expandable_list_item_1,
                new String[]{"TITLE"}, new int[]{android.R.id.text1});

        mListView.setAdapter(adapter);
    }

    private void setupData() {
        getSprintLog();
        getAllReadLog();

        for (SprintLog sprintLog : mSprintLogList) {
            Map<String, String> parentData = new HashMap<>();
            List<Map<String, String>> childList = new ArrayList<>();

            int sprintId = sprintLog.getSprintId();
            int startMonth = sprintLog.getStartDate().get(Calendar.MONTH);
            int startDate = sprintLog.getStartDate().get(Calendar.DATE);
            int endMonth = sprintLog.getEndDate().get(Calendar.MONTH);
            int endDate = sprintLog.getEndDate().get(Calendar.DATE);
            int sumChapters = 0;

            for (ReadLog readLog : mReadLogList) {
                if (readLog.getSprintId() == sprintId) {
                    Calendar calendar = readLog.getDate();
                    int month = calendar.get(Calendar.MONTH);
                    int date = calendar.get(Calendar.DATE);
                    int chapters = readLog.getChapters();
                    sumChapters += chapters;
                    String weekDay = getDayOfWeek(calendar);
                    Map<String, String> childData = new HashMap<>();
                    childData.put("TITLE", String.format("        %d月%d日(%s)                            %d章", month + 1, date, weekDay, chapters));
                    childList.add(childData);
                }
            }

            parentData.put("title", String.format("%d月%d日〜%d月%d日                     %d章", startMonth + 1, startDate, endMonth + 1, endDate, sumChapters));
            parentList.add(parentData);
            allChildList.add(childList);
        }
    }

    private String getDayOfWeek(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return "日";
            case Calendar.MONDAY:
                return "月";
            case Calendar.TUESDAY:
                return "火";
            case Calendar.WEDNESDAY:
                return "水";
            case Calendar.THURSDAY:
                return "木";
            case Calendar.FRIDAY:
                return "金";
            case Calendar.SATURDAY:
                return "土";
        }
        throw new IllegalStateException();
    }

    @OnClick(R.id.add_button)
    public void onClickAddButton() {
        datePicker();
    }

    private void datePicker() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MyDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        final Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day, 0, 0, 0);

                        if (checkIsInSprint(calendar)) {
                            inputNumber(year, month, day);
                        } else {
                            showAlertDialog();
                        }
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.notFoundSprint)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private boolean checkIsInSprint(Calendar calendar) {
        for (SprintLog log : mSprintLogList) {
            long currentDate = calendar.getTimeInMillis();
            long startDate = log.getStartDate().getTimeInMillis();
            long endDate = log.getEndDate().getTimeInMillis();
            if (startDate - 1000 <= currentDate && currentDate <= endDate + 1000) {
                mSprintId = log.getSprintId();
                return true;
            }
        }
        return false;
    }

    private void inputNumber(int year, int month, int day) {
        final EditText editView = new EditText(this);
        editView.setInputType(InputType.TYPE_CLASS_NUMBER);
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);

        new AlertDialog.Builder(this)
                .setTitle(String.format("%d月%d日は何章読みましたか？", month + 1, day))
                .setView(editView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (editView.getText().length() == 0) return;
                        int readNumber = Integer.parseInt(editView.getText().toString());
                        if (readNumber == 0) return;
                        int point = mUser.setRandomEXP(readNumber);
                        mUser.addEXP(getBaseContext(), point);
                        mUser.setDoneChapters(getBaseContext(), mUser.getDoneChapters(getBaseContext()) + readNumber);
                        saveLog(calendar, readNumber, mSprintId);
                        String message = "+" + ((Integer) point).toString() + "EXP";
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void saveLog(Calendar calendar, int readNumber, int sprintId) {
        dbAdapter.open();
        dbAdapter.saveLog(calendar.getTimeInMillis(), readNumber, sprintId);
        dbAdapter.close();
    }

    private void getAllReadLog() {
        mReadLogList.clear();
        dbAdapter.open();
        Cursor cursor = dbAdapter.getReadLog();
        startManagingCursor(cursor);
        if (cursor.moveToFirst()) {
            do {
                ReadLog readLog = new ReadLog(
                        cursor.getInt(cursor.getColumnIndex(DBAdapter.LOG_ID)),
                        cursor.getLong(cursor.getColumnIndex(DBAdapter.LOG_DATE)),
                        cursor.getInt(cursor.getColumnIndex(DBAdapter.LOG_CHAPTERS)),
                        cursor.getInt(cursor.getColumnIndex(DBAdapter.SPRINT_ID)));
                mReadLogList.add(readLog);
            } while (cursor.moveToNext());
        }
        stopManagingCursor(cursor);
        dbAdapter.close();
    }

    private void getSprintLog() {
        mSprintLogList.clear();
        dbAdapter.open();
        Cursor cursor = dbAdapter.getSprintLog();
        startManagingCursor(cursor);
        if (cursor.moveToFirst()) {
            do {
                SprintLog sprintLog = new SprintLog(
                        cursor.getInt(cursor.getColumnIndex(DBAdapter.SPRINT_ID)),
                        cursor.getLong(cursor.getColumnIndex(DBAdapter.START_DATE)),
                        cursor.getLong(cursor.getColumnIndex(DBAdapter.END_DATE)));
                mSprintLogList.add(sprintLog);
            } while (cursor.moveToNext());
        }
        stopManagingCursor(cursor);
        dbAdapter.close();
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

    private void onClickMenu() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        Intent intent_progress = new Intent(HistoryActivity.this, ProgressActivity.class);
                        startActivity(intent_progress);
                        break;
//                    case R.id.menu_profile:
//                        Intent intent_profile = new Intent(HistoryActivity.this, ProfileActivity.class);
//                        startActivity(intent_profile);
//                        break;
                    case R.id.menu_history:
                        Intent intent_history = new Intent(HistoryActivity.this, HistoryActivity.class);
                        startActivity(intent_history);
                        break;
//                    case R.id.menu_ranking:
//                        break;
//                    case R.id.menu_notification:
//                        Intent intent_notification = new Intent(HistoryActivity.this, NotificationActivity.class);
//                        startActivity(intent_notification);
//                        break;
                    case R.id.menu_about:
                        Intent intent_about = new Intent(HistoryActivity.this, AboutAppActivity.class);
                        startActivity(intent_about);
                        break;
//                    case R.id.menu_help:
//                        Intent intent_help = new Intent(HistoryActivity.this, HelpActivity.class);
//                        startActivity(intent_help);
//                        break;
                    case R.id.menu_setting:
                        Intent intent_setting = new Intent(HistoryActivity.this, SettingActivity.class);
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