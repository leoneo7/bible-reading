package leoneo7.biblereading.activity.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leoneo7.biblereading.R;
import leoneo7.biblereading.ReadLog;
import leoneo7.biblereading.activity.menu.AboutAppActivity;
import leoneo7.biblereading.activity.menu.HelpActivity;
import leoneo7.biblereading.activity.menu.NotificationActivity;
import leoneo7.biblereading.activity.menu.SettingActivity;
import leoneo7.biblereading.helper.DBAdapter;
import leoneo7.biblereading.helper.LogAdapter;

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
    ListView mLogListView;

    private ArrayList<ReadLog> mReadLogList = new ArrayList<>();
    private DBAdapter dbAdapter = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_history);
        ButterKnife.bind(this);

        onClickMenu();
        setupToolBar();

        getAll();
        LogAdapter adapter = new LogAdapter(this);
        adapter.setLogList(mReadLogList);
        mLogListView.setAdapter(adapter);
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
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO editText呼び出して数字を入れる
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void getAll() {
        mReadLogList.clear();
        dbAdapter.open();
        Cursor cursor = dbAdapter.getAll();
        startManagingCursor(cursor);
        if (cursor.moveToFirst()) {
            do {
                ReadLog readLog = new ReadLog(
                        cursor.getLong(cursor.getColumnIndex(DBAdapter.LOG_DATE)),
                        cursor.getInt(cursor.getColumnIndex(DBAdapter.LOG_CHAPTERS)));
                mReadLogList.add(readLog);
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
                    case R.id.menu_profile:
                        break;
                    case R.id.menu_history:
                        Intent intent_history = new Intent(HistoryActivity.this, HistoryActivity.class);
                        startActivity(intent_history);
                        break;
                    case R.id.menu_ranking:
                        break;
                    case R.id.menu_notification:
                        Intent intent_notification = new Intent(HistoryActivity.this, NotificationActivity.class);
                        startActivity(intent_notification);
                        break;
                    case R.id.menu_about:
                        Intent intent_about = new Intent(HistoryActivity.this, AboutAppActivity.class);
                        startActivity(intent_about);
                        break;
                    case R.id.menu_help:
                        Intent intent_help = new Intent(HistoryActivity.this, HelpActivity.class);
                        startActivity(intent_help);
                        break;
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